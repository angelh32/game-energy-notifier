package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.energytimer.R
import com.example.energytimer.database.TimerType
import com.example.energytimer.tools.Help


class ShowTypeFragment : DialogFragment() {
	// UI elements
	private lateinit var dialogView: View
	private lateinit var timerNameText: EditText
	private lateinit var gameNameText: EditText
	private lateinit var maxValue: EditText
	private lateinit var tic: EditText

	// Shared data
	private val model: SharedData by activityViewModels()

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bundle = arguments
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			dialogView = inflater.inflate(R.layout.fragment_dialog_create_type, null)
			timerNameText = dialogView.findViewById(R.id.type_name)
			gameNameText = dialogView.findViewById(R.id.game_name)
			maxValue = dialogView.findViewById(R.id.max_value)
			tic = dialogView.findViewById(R.id.tic)
			builder.setView(dialogView)
				.setTitle(R.string.dialog_type)
				.setPositiveButton(R.string.save) { dialog, id ->
					val saveType = TimerType(
						0,
						gameNameText.text.toString(),
						timerNameText.text.toString(),
						"",
						maxValue.text.toString().toInt(),
						tic.text.toString().toInt()
					)
					model.saveType(saveType)
					model.refreshTypes()
				}
				.setNeutralButton(R.string.edit) { dialog, id ->
					Help.printLog("Type", "cancel")
					getDialog()?.cancel()
				}
				.setNegativeButton(R.string.cancel) { dialog, id ->
					Help.printLog("Type", "cancel")
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
		model.selectedType.observe(viewLifecycleOwner, { type ->
			timerNameText.setText(type.typeName)
			gameNameText.setText(type.gameName)
			maxValue.setText("${type.max}")
			tic.setText("${type.tic}")
		})
		super.onViewCreated(view, savedInstanceState)
	}
}