package com.example.energytimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.energytimer.R
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.tools.Help
import com.example.energytimer.tools.IncrementByTicTimer
import java.text.SimpleDateFormat
import java.util.*

class TimerItemAdapter(private val onClick: (CustomTimer) -> Unit) :
	ListAdapter<CustomTimer, TimerItemAdapter.CustomTimerViewHolder>(CustomTimerDiffCallback) {
	class CustomTimerViewHolder(itemView: View, val onClick: (CustomTimer) -> Unit) :
		RecyclerView.ViewHolder(itemView) {

		private val timerName: TextView = itemView.findViewById(R.id.type_name)
		private val description: TextView = itemView.findViewById(R.id.description)
		private val startDateText: TextView = itemView.findViewById(R.id.start_date)
		private val finishDateText: TextView = itemView.findViewById(R.id.finish_date)
		private val showTime: TextView = itemView.findViewById(R.id.show_time)
		private val maxValue: TextView = itemView.findViewById(R.id.max_value)
		private val nextCount: TextView = itemView.findViewById(R.id.next_count)
		private lateinit var currentTimer: CustomTimer

		init {
			itemView.setOnClickListener {
				onClick(currentTimer)
			}
		}


		fun bind(timer: CustomTimer) {
			currentTimer = timer
			val myTimer = IncrementByTicTimer(currentTimer)
			if (!myTimer.isTimerRunning) {
				myTimer.startTimer()
			}
			timerName.text = timer.timerName
			description.text = timer.description
			myTimer.totalTimeLeftLabel.observeForever { label -> showTime.setText(label) }
			myTimer.currentTimeLabel.observeForever { label -> nextCount.setText(label) }
			myTimer.totalGeneratedLabel.observeForever { label -> maxValue.setText(label) }
			startDateText.text = Help.formatFromLong(timer.finishDate)
			finishDateText.text = Help.formatFromLong(timer.finishDate)
		}
	}


	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): CustomTimerViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.card_timer, parent, false)
		return CustomTimerViewHolder(view, onClick)
	}

	override fun onBindViewHolder(holder: CustomTimerViewHolder, position: Int) {
		val timer = getItem(position)
		holder.bind(timer)
	}
}

object CustomTimerDiffCallback : DiffUtil.ItemCallback<CustomTimer>() {
	override fun areItemsTheSame(oldItem: CustomTimer, newItem: CustomTimer): Boolean {
		return oldItem == newItem
	}

	override fun areContentsTheSame(oldItem: CustomTimer, newItem: CustomTimer): Boolean {
		return oldItem.timerId == newItem.timerId
	}

}
