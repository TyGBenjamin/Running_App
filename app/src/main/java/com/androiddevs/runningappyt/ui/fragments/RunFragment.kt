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
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.adapters.RunAdapter
import com.androiddevs.runningappyt.databinding.FragmentRunBinding
import com.androiddevs.runningappyt.other.Constants.DYNEG
import com.androiddevs.runningappyt.other.Constants.DYPOS
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

        rvRuns.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // if the recycler view is scrolled
                // above hide the FAB
                if (dy > DYPOS && fab.isShown) {
                    fab.hide()
                }

                // if the recycler view is
                // scrolled above show the FAB
                if (dy < DYNEG && !fab.isShown) {
                    fab.show()
                }

                // of the recycler view is at the first
                // item always show the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.show()
                }
            }
        })
    }

    private fun setupRecyclerView() = with(binding) {
        rvRuns.apply {
            adapter = runAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
