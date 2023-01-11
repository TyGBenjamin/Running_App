package com.androiddevs.runningappyt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.adapters.RunAdapter
import com.androiddevs.runningappyt.databinding.FragmentRunBinding
import com.androiddevs.runningappyt.other.SortType
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * [Fragment] to view details of all finished runs.
 *
 * @constructor Create instance of [RunFragment]
 */
@AndroidEntryPoint

class RunFragment : Fragment() {

    private var _binding: FragmentRunBinding? = null
    val binding: FragmentRunBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    private val runAdapter: RunAdapter by lazy { RunAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentRunBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
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

    private fun setupRecyclerView() = with(binding) {
        rvRuns.apply {
            adapter = runAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
