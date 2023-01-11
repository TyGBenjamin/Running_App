@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.ItemRunBinding
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.Constants
import com.androiddevs.runningappyt.other.TrackingUtility
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * [RecyclerView.Adapter] to display [List] of [Run].
 *
 * @constructor Create instance of [RunAdapter]
 */
class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    private var _binding: ItemRunBinding? = null
    private val binding: ItemRunBinding get() = _binding!!

    private val tvAvgSpeed by lazy { binding.tvAvgSpeed }
    private val tvDate by lazy { binding.tvDate }
    private val tvCalories by lazy { binding.tvCalories }
    private val tvDistance by lazy { binding.tvDistance }
    private val ivRunImage by lazy { binding.ivRunImage }

    /**
     * Run view holder.
     *
     * @constructor Create instance of [RunViewHolder]
     *
     * @param itemView
     */
    class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(binding.ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            binding.tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${run.distanceInMeters / Constants.Float.THOUSAND}km"
            binding.tvDistance.text = distanceInKm

            binding.tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            binding.tvCalories.text = caloriesBurned
        }
    }
}
