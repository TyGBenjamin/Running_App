@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.FragmentTrackingBinding
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.Constants.ACTION_PAUSE_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_STOP_SERVICE
import com.androiddevs.runningappyt.other.Constants.Float.THOUSAND
import com.androiddevs.runningappyt.other.Constants.MAP_ZOOM
import com.androiddevs.runningappyt.other.Constants.POLYLINE_COLOR
import com.androiddevs.runningappyt.other.Constants.POLYLINE_WIDTH
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.services.Polyline
import com.androiddevs.runningappyt.services.PolylineList
import com.androiddevs.runningappyt.services.TrackingService
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round
import javax.inject.Inject
import java.util.Calendar

/**
 * [Fragment] to handle map related tasks used for tracking run.
 *
 * @constructor Create instance of [TrackingFragment]
 */
@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private var _binding: FragmentTrackingBinding? = null
    val binding: FragmentTrackingBinding get() = _binding!!
    val tvTimer by lazy { binding.tvTimer }
    val mapView by lazy { binding.mapView }

    private val viewModel: MainViewModel by viewModels()

    private var currentlyTracking = false
    private var currentPathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var curTimeInMillis = 0L

    private var menu: Menu? = null

    @set:Inject
    var weight = 80f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentTrackingBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        val bgLocPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) showDeniedPermissionDialog()
            }
        val fineLocPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    permissionMap[Manifest.permission.ACCESS_FINE_LOCATION]!!
                ) {
                    checkPermissions(null, bgLocPermissionLauncher)
                } else if (!permissionMap[Manifest.permission.ACCESS_FINE_LOCATION]!!) {
                    showDeniedPermissionDialog()
                }
            }
        checkPermissions(fineLocPermissionLauncher, bgLocPermissionLauncher)

        mapView.onCreate(savedInstanceState)
        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        btnFinishRun.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }

        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() = with(TrackingService) {
        isTracking.observe(viewLifecycleOwner) { isTracking: Boolean ->
            updateTracking(isTracking)
        }
        pathPoints.observe(viewLifecycleOwner) { pathPoints: PolylineList ->
            currentPathPoints = pathPoints
            addLatestPolyline()
            moveCameraToUser()
        }
        timeRunInMillis.observe(viewLifecycleOwner) { timeRunInMillis: Long ->
            curTimeInMillis = timeRunInMillis
            tvTimer.text = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
        }
    }

    private fun toggleRun() {
        if (currentlyTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun checkPermissions(
        fineLocLauncher: ActivityResultLauncher<Array<String>>?,
        bgLocLauncher: ActivityResultLauncher<String>
    ) {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        val permissionDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        if (
            fineLocLauncher != null &&
            !TrackingUtility.checkPermissions(
                requireContext(),
                listOf(Manifest.permission.ACCESS_FINE_LOCATION)
            )
        ) {
            permissionDialog.setMessage(getString(R.string.fineLocDialogTest))
            permissionDialog.setPositiveButton(getString(R.string.permissionDialogPos)) { _, _ ->
                fineLocLauncher!!.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionDialog.setMessage(getString(R.string.bgLocDialogText))
            permissionDialog.setPositiveButton(getString(R.string.permissionDialogPos)) { _, _ ->
                bgLocLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
        permissionDialog.setNegativeButton(getString(R.string.permissionDialogNeg)) { _, _ -> }
            .create()
            .show()
    }

    private fun showDeniedPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(getString(R.string.permissionDialogDeniedText))
            .setNegativeButton(getString(R.string.permissionDialogDeniedNeg)) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
    }

    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (curTimeInMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miCancelTracking -> showCancelTrackingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ -> stopRun() }
            .setNegativeButton("No") { dialogInterface, _ -> dialogInterface.cancel() }
            .create()
            .show()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun updateTracking(isTracking: Boolean) = with(binding) {
        currentlyTracking = isTracking
        if (!isTracking) {
            btnToggleRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        } else {
            btnToggleRun.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if (currentPathPoints.isNotEmpty() && currentPathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(currentPathPoints.last().last(), MAP_ZOOM)
            )
        }
    }

    private fun zoomToSeeWholeTrack() = with(LatLngBounds.Builder()) {
        for (polyline in currentPathPoints) {
            for (pos in polyline) include(pos)
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                build(),
                mapView.width,
                mapView.height,
                (mapView.height * MAP_HEIGHT_BOUND).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in currentPathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed =
                round(distanceInMeters / THOUSAND / (curTimeInMillis / THOUSAND / SIXTY / SIXTY) * TEN) / TEN
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = (distanceInMeters / THOUSAND * weight).toInt()
            val run = Run(
                img = bmp,
                timestamp = dateTimestamp,
                avgSpeedInKMH = avgSpeed,
                distanceInMeters = distanceInMeters,
                timeInMillis = curTimeInMillis,
                caloriesBurned = caloriesBurned
            )
            viewModel.saveRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }

    private fun addAllPolylines() {
        for (polyline in currentPathPoints) {
            PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
                .let { map?.addPolyline(it) }
        }
    }

    private fun addLatestPolyline() {
        if (currentPathPoints.isNotEmpty() && currentPathPoints.last().size > 1) {
            val preLastLatLng = currentPathPoints.last()[currentPathPoints.last().size - 2]
            val lastLatLng = currentPathPoints.last().last()
            PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
                .let { map?.addPolyline(it) }
        }
    }

    private fun sendCommandToService(action: String) = with(requireContext()) {
        Intent(this, TrackingService::class.java)
            .also { it.action = action }
            .let { startService(it) }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        private const val MAP_HEIGHT_BOUND = 0.05f
        private const val SIXTY = 60
        private const val TEN = 10
    }
}
