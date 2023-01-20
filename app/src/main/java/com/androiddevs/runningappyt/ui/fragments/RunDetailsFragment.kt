package com.androiddevs.runningappyt.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.ui.viewmodels.RunDetailsViewModel
import com.androiddevs.runningappyt.ui.viewmodels.RunDetailsViewModel.Companion.div
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * Run details fragment.
 *
 * @constructor Create empty Run details fragment
 */
@AndroidEntryPoint
class RunDetailsFragment : Fragment() {
    private val args: RunDetailsFragmentArgs by navArgs()
    val runViewModel by activityViewModels<RunDetailsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch { runViewModel.getRunById(args.runnerID) }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    ShowRunDetailsVM(args.runnerID)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Suppress("LongMethod")
    @Composable
    fun ShowRunDetailsVM(runnerID: Int) {
        var (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
        runViewModel.getRunById(runnerID)
        val run by runViewModel.run.collectAsState()
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (run != null) {
                        val simpleDateFormat = SimpleDateFormat(stringResource(R.string.dateFormat))
                        val dateString = simpleDateFormat.format(run!!.timestamp)
                        Text(text = "Time Stamp: $dateString ")
                    }
                    if (run != null) {
                        val timeRan = run?.timeInMillis?.div(div)
                        Text(text = " Time Ran: $timeRan(s)")
                    }
                }
                DisplayText(run = run)
                run?.img?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.contentDes)
                    )
                }
                Row {
                    var (hiddenVisibleState, setHiddenState) = remember {
                        mutableStateOf(false)
                    }
                    ButtonOne()
                    if (!hiddenVisibleState) {
                        ButtonDelete {
                            setSnackBarState(!snackbarVisibleState)
                            setHiddenState(!hiddenVisibleState)
                        }
                    }
                    if (snackbarVisibleState) {
                        Snackbar(
                            action = {
                                Column() {
                                    Button(onClick = {
                                        runViewModel.deleteRun(run!!)
                                        findNavController().navigateUp()
                                    }) {
                                        Text(text = stringResource(R.string.deleteRun))
                                    }
                                    Button(onClick = {
                                        setSnackBarState(!snackbarVisibleState)
                                        setHiddenState(!hiddenVisibleState)
                                    }) {
                                        Text(text = stringResource(R.string.cancel))
                                    }
                                    Row() {
                                        Text(text = stringResource(R.string.confirmDelete))
                                    }
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row() {
                                Text(text = stringResource(R.string.confirmDelete))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayText(run: Run?) {
        DividerOne()
        Text(text = "Distance (m): ${run?.distanceInMeters}")
        Text(text = "Calories burned: ${run?.caloriesBurned}")
        Text(text = "Avg Speed(km/h): ${run?.avgSpeedInKMH}")
        DividerOne()
    }

    @Composable
    fun ButtonOne() {
        Button(
            modifier = Modifier.padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                disabledContainerColor = Color.Gray
            ),
            onClick = {
                findNavController().navigateUp()
            }
        ) {
            Text(text = stringResource(R.string.back), color = Color.White)
        }
    }

    @Composable
    fun ButtonDelete(onClick: () -> Unit) {
        Button(
            modifier = Modifier.padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                disabledContainerColor = Color.Gray
            ),
            onClick = {
                onClick()
            }
        ) {
            Text(text = stringResource(R.string.deleteRun), color = Color.White)
        }
    }
}

/**
 *
 */
@Composable
fun DividerOne() {
    Divider(
        color = Color.White.copy(alpha = 0.3f),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 48.dp)
    )
}
