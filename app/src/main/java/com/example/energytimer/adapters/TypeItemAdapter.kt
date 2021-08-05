package com.example.energytimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.energytimer.R
import com.example.energytimer.database.TimerType
import com.example.energytimer.tools.Help
import java.text.SimpleDateFormat
import java.util.*

class TypeItemAdapter(private val onClick: (TimerType) -> Unit) :
	ListAdapter<TimerType, TypeItemAdapter.TimerTypeViewHolder>(TimerTypeDiffCallback) {
	class TimerTypeViewHolder(itemView: View, val onClick: (TimerType) -> Unit) :
		RecyclerView.ViewHolder(itemView) {

		private val typeName: TextView = itemView.findViewById(R.id.type_name)
		private val typeDescription: TextView = itemView.findViewById(R.id.type_description)
		private val typeMax : TextView = itemView.findViewById(R.id.type_max)
		private val typeTic: TextView = itemView.findViewById(R.id.type_tic)

		private lateinit var currentType: TimerType

		init {
			itemView.setOnClickListener {
				onClick(currentType)
			}
		}

		fun bind(type: TimerType) {
			currentType = type
			typeName.text = type.typeName
			typeDescription.text = type.description
			typeMax.text = type.max.toString()
			typeTic.text = Help.formatFromSeconds(type.tic.toLong())
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): TimerTypeViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.card_type, parent, false)
		return TimerTypeViewHolder(view, onClick)
	}

	override fun onBindViewHolder(holder: TimerTypeViewHolder, position: Int) {
		val timer = getItem(position)
		holder.bind(timer)
	}
}

object TimerTypeDiffCallback : DiffUtil.ItemCallback<TimerType>() {
	override fun areItemsTheSame(oldItem: TimerType, newItem: TimerType): Boolean {
		return oldItem == newItem
	}

	override fun areContentsTheSame(oldItem: TimerType, newItem: TimerType): Boolean {
		return oldItem.typeId == newItem.typeId
	}

}
