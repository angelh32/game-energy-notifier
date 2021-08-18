package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.energytimer.R
import com.example.energytimer.tools.Help
import com.example.energytimer.tools.IncrementByTimer


class ShowTimerFragment : DialogFragment() {
	// UI elements
	private lateinit var dialogView: View
	private lateinit var totalTime: TextView
	private lateinit var currentValue: TextView
	private lateinit var currentTime: TextView
	private lateinit var maxValue: TextView
	private lateinit var timeGenerate: TextView
	private lateinit var startDate: TextView
	private lateinit var finishDate: TextView
	private lateinit var timerName: TextView
	private lateinit var gameName: TextView
	private lateinit var timerDescription: TextView

	// Shared data
	private val model: SharedData by activityViewModels()

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			/* ----------- Dynamic elements ----------- */
			dialogView = inflater.inflate(R.layout.fragment_dialog_show_timer, null)
			totalTime = dialogView.findViewById(R.id.st_total_time)
			currentValue = dialogView.findViewById(R.id.st_current_value)
			currentTime = dialogView.findViewById(R.id.st_current_time)
			maxValue = dialogView.findViewById(R.id.st_max_value)
			timeGenerate = dialogView.findViewById(R.id.st_time_generate)
			startDate = dialogView.findViewById(R.id.st_start)
			finishDate = dialogView.findViewById(R.id.st_finish)
			timerName = dialogView.findViewById(R.id.st_timer_name)
			gameName = dialogView.findViewById(R.id.st_game_name)
			timerDescription = dialogView.findViewById(R.id.st_description)

			builder.setView(dialogView)
				.setPositiveButton(R.string.restart) { dialog, id ->
					Help.printLog("Type", "Save")
					var customTimer = Help.createEmptyTimer()
				}
				.setNeutralButton(R.string.edit) { dialog, id ->
					EditTimerFragment().show(parentFragmentManager, "TIMER")
					Help.printLog("Type", "cancel")
					getDialog()?.cancel()
				}
				.setNegativeButton(R.string.delete) { dialog, id ->
					val alertDialog: AlertDialog = requireActivity().let {
						val builder = AlertDialog.Builder(it)
						builder.apply {
							setMessage(R.string.delete_this_timer)
							setPositiveButton(R.string.confirm) { dialog, id ->
								model.deleteTimer(model.selectedTimer.value!!)
								model.refreshTimers()
							}
							setNegativeButton(R.string.cancel) { dialog, id ->
								getDialog()?.cancel()
							}
						}
						builder.create()
					}
					alertDialog.show()
					getDialog()?.cancel()
				}
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return dialogView
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		model.selectedTimer.observe(viewLifecycleOwner, { timer ->
			val currentType = model.selectedType.value!!
			val myTimer = IncrementByTimer(timer)
			if (!myTimer.isTimerRunning) {
				myTimer.startTimer()
			}
			myTimer.totalTimeLeftLabel.observe(viewLifecycleOwner, { label -> totalTime.text = label })
			myTimer.currentTimeLabel.observe(viewLifecycleOwner, { label -> currentTime.text = label })
			myTimer.totalGeneratedLabel.observe(
				viewLifecycleOwner,
				{ label -> currentValue.text = label })
			timerName.text = timer.timerName
			maxValue.text = currentType.max.toString()
			timeGenerate.text = Help.formatFromSeconds(currentType.tic.toLong())
			startDate.text = Help.formatFromLong(timer.startDate)
			finishDate.text = Help.formatFromLong(timer.finishDate)
			maxValue.text = currentType.max.toString()
			timerName.text = timer.timerName
			timerDescription.text = timer.description
			gameName.text = currentType.gameName
		})
		super.onViewCreated(view, savedInstanceState)
	}
}
