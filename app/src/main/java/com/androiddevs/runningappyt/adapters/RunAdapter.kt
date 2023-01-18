@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.databinding.ItemRunBinding
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.Constants
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.ui.fragments.RunFragmentDirections
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.android.synthetic.main.item_run.view.ivRunImage
import kotlinx.android.synthetic.main.item_run.view.tvAvgSpeed
import kotlinx.android.synthetic.main.item_run.view.tvCalories
import kotlinx.android.synthetic.main.item_run.view.tvDate
import kotlinx.android.synthetic.main.item_run.view.tvDistance
import kotlinx.android.synthetic.main.item_run.view.tvTime

/**
 * [RecyclerView.Adapter] to display [List] of [Run].
 *
 * @constructor Create instance of [RunAdapter]
 */
class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    /**
     * Run view holder.
     *
     * @property binding
     * @constructor Create empty Run view holder
     */
    class RunViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    /**
     * Used to update adapter with new [List] of [Run].
     *
     * @param list to be displayed
     */
    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val itemBinding = ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            setOnClickListener {
                it.setOnClickListener { println("I have been clicked with ${run.id}")
                    val action = RunFragmentDirections.actionRunFragmentToRunDetailsFragment(run.id)
                findNavController().navigate(action)}
            }
            Glide.with(this).load(run.img).into(ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${run.distanceInMeters / Constants.Float.THOUSAND}km"
            tvDistance.text = distanceInKm

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            tvCalories.text = caloriesBurned
        }
    }
}
