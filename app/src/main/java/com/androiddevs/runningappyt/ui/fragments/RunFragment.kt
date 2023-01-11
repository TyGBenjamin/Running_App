package com.androiddevs.runningappyt.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.adapters.RunAdapter
import com.androiddevs.runningappyt.other.SortType
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.fab
import kotlinx.android.synthetic.main.fragment_run.rvRuns
import kotlinx.android.synthetic.main.fragment_run.spFilter

/**
 * [Fragment] to view details of all finished runs.
 *
 * @constructor Create instance of [RunFragment]
 */
@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {
    private val viewModel: MainViewModel by viewModels()

    private val runAdapter: RunAdapter by lazy { RunAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        when (viewModel.sortType) {
            SortType.DATE -> spFilter.setSelection(viewModel.sortType.ordinal)
            SortType.RUNNING_TIME -> spFilter.setSelection(viewModel.sortType.ordinal)
            SortType.DISTANCE -> spFilter.setSelection(viewModel.sortType.ordinal)
            SortType.AVG_SPEED -> spFilter.setSelection(viewModel.sortType.ordinal)
            SortType.CALORIES_BURNED -> spFilter.setSelection(viewModel.sortType.ordinal)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                when (pos) {
                    SortType.DATE.ordinal -> viewModel.sortRuns(SortType.DATE)
                    SortType.RUNNING_TIME.ordinal -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    SortType.DISTANCE.ordinal -> viewModel.sortRuns(SortType.DISTANCE)
                    SortType.AVG_SPEED.ordinal -> viewModel.sortRuns(SortType.AVG_SPEED)
                    SortType.CALORIES_BURNED.ordinal -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() = rvRuns.apply {
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}
