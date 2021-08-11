package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.energytimer.R
import com.example.energytimer.database.TimerType
import com.example.energytimer.tools.Help


class EditTypeFragment : DialogFragment() {
	// UI elements
	private lateinit var dialogView: View
	private lateinit var timerNameText: EditText
	private lateinit var gameNameText: EditText
	private lateinit var typeDescription: EditText
	private lateinit var maxValue: EditText
	private lateinit var daysPicker: NumberPicker
	private lateinit var hoursPicker: NumberPicker
	private lateinit var minutesPicker: NumberPicker
	private lateinit var secondsPicker: NumberPicker

	// Shared data
	private val model: SharedData by activityViewModels()
	private lateinit var currentType: TimerType
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			dialogView = inflater.inflate(R.layout.fragment_dialog_create_type, null)
			timerNameText = dialogView.findViewById(R.id.type_name)
			gameNameText = dialogView.findViewById(R.id.game_name)
			typeDescription = dialogView.findViewById(R.id.ctf_description)
			maxValue = dialogView.findViewById(R.id.max_value)
			daysPicker = dialogView.findViewById(R.id.ctf_days)
			daysPicker.maxValue = 30
			hoursPicker = dialogView.findViewById(R.id.ctf_hours)
			hoursPicker.maxValue = 23
			minutesPicker = dialogView.findViewById(R.id.ctf_minutes)
			minutesPicker.maxValue = 59
			secondsPicker = dialogView.findViewById(R.id.ctf_seconds)
			secondsPicker.maxValue = 59
//			tic = dialogView.findViewById(R.id.tic)
			builder.setView(dialogView)
				.setPositiveButton(R.string.update) { _, _ ->
					val forSave = buildTypeFromUi()
					if (currentType.typeId == 0) {
						model.saveType(forSave)
					} else {
						forSave.typeId = currentType.typeId
						model.updateType(forSave)
					}
					model.refreshTypes()
				}
				.setNeutralButton(R.string.delete) { dialog, _ ->
					val alertDialog: AlertDialog = requireActivity().let {
						val alertBuilder = AlertDialog.Builder(it)
						alertBuilder.apply {
							setMessage(R.string.delete_this_type)
							setPositiveButton(R.string.save) { _, _ ->
								model.deleteType(model.selectedType.value!!)
								model.refreshTypes()
							}
							setNegativeButton(R.string.cancel) { dialog, _ ->
								dialog.cancel()
							}
						}
						alertBuilder.create()
					}
					alertDialog.show()
					dialog.cancel()
				}
				.setNegativeButton(R.string.cancel) { dialog, _ ->
					dialog.cancel()
				}
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}

	private fun buildTypeFromUi(): TimerType {
		val fromUi = Help.createEmptyType()
		fromUi.gameName = gameNameText.text.toString()
		fromUi.typeName = timerNameText.text.toString()
		fromUi.description = typeDescription.text.toString()
		fromUi.max = maxValue.text.toString().toInt()
		val ticList = listOf(
			daysPicker.value,
			hoursPicker.value,
			minutesPicker.value,
			secondsPicker.value
		).map { value -> value.toLong() }
		fromUi.tic = Help.parseMillisecondsFromArray(ticList).toInt() / 1000
		return fromUi
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return dialogView
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		model.selectedType.observe(viewLifecycleOwner, { type ->
			currentType = type
			timerNameText.setText(type.typeName)
			gameNameText.setText(type.gameName)
			typeDescription.setText(type.description)
			maxValue.setText("${type.max}")
			val timeValues = Help.splitFromSeconds(type.tic.toLong())
			daysPicker.value = timeValues[0]
			hoursPicker.value = timeValues[1]
			minutesPicker.value = timeValues[2]
			secondsPicker.value = timeValues[3]
		})
		super.onViewCreated(view, savedInstanceState)
	}
}