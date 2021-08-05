package com.example.energytimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.example.energytimer.R
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.database.TimerType
import com.example.energytimer.database.TimerTypeDao
import com.example.energytimer.tools.DatabaseName
import com.example.energytimer.tools.Help
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


class ShowTypeFragment : DialogFragment() {
	private lateinit var db: LocalDatabase
	private lateinit var current: TimerType
	private var currentId = 0
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bundle = arguments
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater
			if (bundle != null) {
				currentId = bundle.getInt("current")
			}
			getCurrent(requireContext())
			val view = inflater.inflate(R.layout.fragment_dialog_create_type, null)
			val timerNameText = view.findViewById<EditText>(R.id.type_name)
			val gameNameText = view.findViewById<EditText>(R.id.game_name)
			val maxValue = view.findViewById<EditText>(R.id.max_value)
			val tic = view.findViewById<EditText>(R.id.tic)
			timerNameText.setText(current.typeName)
			gameNameText.setText(current.gameName)
			maxValue.setText("${current.max}")
			tic.setText("${current.tic}")

			builder.setView(view)
				.setTitle(R.string.dialog_type)
				.setPositiveButton(R.string.save) { dialog, id ->
					Help.printLog("Type", "Save")
					current.typeName = timerNameText.text.toString()
					current.gameName = gameNameText.text.toString()
					current.max = maxValue.text.toString().toInt()
					current.tic = tic.text.toString().toInt()
					saveCurrent(current)
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

	private fun saveCurrent(current: TimerType) = runBlocking {
		thread {
			val timerDao: TimerTypeDao = db.timerTypeDao()
			timerDao.insertAll(current)
		}
	}

	private fun getCurrent(context: Context) {
		db = Room.databaseBuilder(
			context,
			LocalDatabase::class.java,
			DatabaseName
		)
			.allowMainThreadQueries()
			.build()
		val timerDao: TimerTypeDao = db.timerTypeDao()
		val fromDb = timerDao.findById(currentId)
		if (fromDb == null) {
			current = TimerType(0, "", "", "", 0, 0)
		} else {
			current = fromDb
		}
	}
//	companion object {
//		const val TAG = "PurchaseConfirmationDialog"
//	}
}