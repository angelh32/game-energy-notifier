package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.example.energytimer.R
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.database.TimerType
import com.example.energytimer.tools.DatabaseName
import com.example.energytimer.tools.Help
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.thread


class ShowTimerFragment : DialogFragment() {
	lateinit var db: LocalDatabase
	lateinit var current: CustomTimer
	lateinit var typesList: List<TimerType>
	var currentId = 0
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bundle = arguments
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			if (bundle != null) {
				currentId = bundle.getInt("current")
			}
			getCurrent(requireContext())
			val view = inflater.inflate(R.layout.fragment_dialog_create_timer, null)

			/* ----------- Dinamic elements ----------- */
			val gameSpinner = view.findViewById<Spinner>(R.id.game_spinner)
			val timerTypeSpinner = view.findViewById<Spinner>(R.id.timer_type_spinner)
			val timerName = view.findViewById<EditText>(R.id.timer_name)
			val timerDescription = view.findViewById<EditText>(R.id.timer_description)
			val currentValue = view.findViewById<SeekBar>(R.id.initial_value)
			val seekBarValue = view.findViewById<TextView>(R.id.seekbar_value)

			var gameList = typesList.map { item -> item.gameName }
			gameList = gameList.toSet().toList()
			val adapter = ArrayAdapter(
				requireContext(),
				android.R.layout.simple_spinner_dropdown_item,
				gameList
			)
			gameSpinner.adapter = adapter
			lateinit var typeList: List<TimerType>
			lateinit var selectedType: TimerType
			gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
					val selectedGame = gameList[pos]
					typeList = typesList.filter { item -> item.gameName.equals(selectedGame) }
					val typeListString = typeList.map { item -> item.typeName }
					val adapterType = ArrayAdapter(
						requireContext(),
						android.R.layout.simple_spinner_dropdown_item,
						typeListString
					)
					timerTypeSpinner.adapter = adapterType
				}

				override fun onNothingSelected(parent: AdapterView<*>) {}
			}
			timerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
					selectedType = typeList[pos]
					currentValue.setProgress(current.initial)
					currentValue.setMax(selectedType.max)
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

			builder.setView(view)
				.setTitle(R.string.add_new_timer)
				.setPositiveButton(R.string.save) { dialog, id ->
					Help.printLog("Type", "Save")
					val typeId = selectedType.typeId
					val timerNameString = timerName.text.toString()
					val timerDesc = timerDescription.text.toString()
					val initialValue = seekBarValue.text.toString().toInt()
					val max = selectedType.max
					val tic = selectedType.tic
					val startdate = Date().time
					val finishDate = startdate + (initialValue * tic * 1000)
					saveCurrent(
						CustomTimer(
							0,
							typeId,
							timerNameString,
							timerDesc,
							initialValue,
							max,
							tic,
							startdate,
							finishDate
						)
					)
				}
				.setNeutralButton(R.string.edit) { dialog, id ->
					Help.printLog("Type", "cancel")
					getDialog()?.cancel()
				}
				.setNegativeButton(R.string.start) { dialog, id ->
					Help.printLog("Type", "negatinve")
					getDialog()?.cancel()
				}
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}

	override fun onDestroy() {
		db.close()
		super.onDestroy()
	}

	fun saveCurrent(current: CustomTimer) = runBlocking {
		thread {
			val timerDao = db.customTimerDao()
			timerDao.insertAll(current)
		}
	}

	fun getCurrent(context: Context) {
		db = Room.databaseBuilder(
			context,
			LocalDatabase::class.java,
			DatabaseName
		)
			.allowMainThreadQueries()
			.build()
		val timerDao = db.customTimerDao()
		val typeDao = db.timerTypeDao()
		typesList = typeDao.getAll()
		val fromDb = timerDao.findById(currentId)
		if (fromDb == null) {
			current = CustomTimer(0, 0, "", "", 0, 0, 0, 0, 0)
		} else {
			current = fromDb
		}
	}
//	companion object {
//		const val TAG = "PurchaseConfirmationDialog"
//	}
}