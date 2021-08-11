package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.energytimer.R
import com.example.energytimer.database.TimerType
import com.example.energytimer.tools.Help
import java.util.*


class ShowTimerFragment : DialogFragment() {
	// UI elements
	private lateinit var dialogView: View
	private lateinit var gameSpinner: Spinner
	private lateinit var timerTypeSpinner: Spinner
	private lateinit var timerName: EditText
	private lateinit var timerDescription: EditText
	private lateinit var currentValue: SeekBar
	private lateinit var seekBarValue: TextView

	// adapters
	private lateinit var gameListAdapter: ArrayAdapter<String>
	private lateinit var typesListAdapter: ArrayAdapter<String>

	// Shared data
	private val model: SharedData by activityViewModels()
	private lateinit var allTypes: List<TimerType>
	private lateinit var gamesForSpinnerLabels: List<String>
	private lateinit var typesForSpinner: List<TimerType>
	private lateinit var typesForSpinnerLabels: List<String>
	private lateinit var selectedType: TimerType

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			/* ----------- Dynamic elements ----------- */
			dialogView = inflater.inflate(R.layout.fragment_dialog_create_timer, parentFragment)
			gameSpinner = dialogView.findViewById(R.id.game_spinner)
			timerTypeSpinner = dialogView.findViewById(R.id.timer_type_spinner)
			timerName = dialogView.findViewById(R.id.timer_name)
			timerDescription = dialogView.findViewById(R.id.timer_description)
			currentValue = dialogView.findViewById(R.id.initial_value)
			seekBarValue = dialogView.findViewById(R.id.seekbar_value)

			gameListAdapter = ArrayAdapter(
				requireContext(),
				android.R.layout.simple_spinner_dropdown_item,
				arrayListOf()
			)
			gameSpinner.adapter = gameListAdapter

			typesListAdapter = ArrayAdapter(
				requireContext(),
				android.R.layout.simple_spinner_dropdown_item,
				arrayListOf()
			)
			timerTypeSpinner.adapter = typesListAdapter

			gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
					val selectedGame = gamesForSpinnerLabels[pos]
					typesForSpinner = allTypes.filter { item -> item.gameName.equals(selectedGame) }
					typesForSpinnerLabels = typesForSpinner.map { item -> item.typeName }
					setListToSpinnerAdapter(typesListAdapter, typesForSpinnerLabels)
				}

				override fun onNothingSelected(parent: AdapterView<*>) {}
			}
			timerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
					selectedType = typesForSpinner[pos]
					Help.printLog(selectedType)
					currentValue.progress = 1
					currentValue.max = selectedType.max
					timerName.setText(selectedType.typeName)
					timerDescription.setText(selectedType.description)
				}

				override fun onNothingSelected(parent: AdapterView<*>) {}
			}
			currentValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
				override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
					// Display the current progress of SeekBar
					seekBarValue.text = "$i"
				}

				override fun onStartTrackingTouch(seekBar: SeekBar) {
					// Do something
				}

				override fun onStopTrackingTouch(seekBar: SeekBar) {
					// Do something
				}
			})

			builder.setView(dialogView)
				.setTitle(R.string.add_new_timer)
				.setPositiveButton(R.string.save) { dialog, id ->
					Help.printLog("Type", "Save")
					var customTimer = Help.createEmptyTimer()
					customTimer.timerId = selectedType.typeId
					customTimer.typeId = selectedType.typeId
					customTimer.timerName = timerName.text.toString()
					customTimer.description = timerDescription.text.toString()
					customTimer.initial = seekBarValue.text.toString().toInt()
					customTimer.max = selectedType.max
					customTimer.tic = selectedType.tic
					customTimer.startDate = Date().time
					customTimer.finishDate = customTimer.startDate + (customTimer.initial * customTimer.tic * 1000)
					model.saveTimer(customTimer)
					model.refreshTimers()
				}
				.setNeutralButton(R.string.edit) { dialog, id ->
					Help.printLog("Type", "cancel")
					getDialog()?.cancel()
				}
				.setNegativeButton(R.string.start) { dialog, id ->
					Help.printLog("Type", "negative")
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

	fun setListToSpinnerAdapter(adapter: ArrayAdapter<String>, list: List<String>) {
		adapter.clear()
		adapter.addAll(list)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		allTypes = model.typelist.value!!
		val games = allTypes.map { item -> item.gameName }
		gamesForSpinnerLabels = games.toSet().toList()
		setListToSpinnerAdapter(gameListAdapter, gamesForSpinnerLabels)
		val types = allTypes.map { item -> item.typeName }
		typesForSpinnerLabels = types.toSet().toList()
		setListToSpinnerAdapter(typesListAdapter, typesForSpinnerLabels)
		model.selectedTimer.observe(viewLifecycleOwner, { timer ->
			Help.printLog("dial", timer.toString())
		})
		super.onViewCreated(view, savedInstanceState)
	}
}
