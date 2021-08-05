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

		/* Bind flower name and image. */
		fun bind(timer: CustomTimer) {
			currentTimer = timer
			val myTimer = IncrementByTicTimer(currentTimer) { timeLeft -> updateText(timeLeft) }
			if (!myTimer.isTimerRunning) {
				myTimer.startTimer()
			}
			timerName.text = timer.timerName
			description.text = timer.description
			val pattern = "MM-dd-yyyy hh:mm"
			val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
			startDateText.text = simpleDateFormat.format(Date(timer.startDate))
			finishDateText.text = simpleDateFormat.format(Date(timer.finishDate))
		}

		private fun updateText(response: Array<String>) {
			showTime.text = response[0]
			nextCount.text = response[1]
			maxValue.text = response[2]
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
