package com.example.energytimer.fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.energytimer.database.*
import com.example.energytimer.tools.DatabaseName

class SharedData : ViewModel() {
	// Database
	private lateinit var db: LocalDatabase
	private lateinit var typeDao: TimerTypeDao
	private lateinit var timerDao: CustomTimerDao

	// Lists
	val timerList = MutableLiveData<List<CustomTimer>>()
	val typelist = MutableLiveData<List<TimerType>>()

	//Selected
	val selectedTimer = MutableLiveData<CustomTimer>()
	val selectedType = MutableLiveData<TimerType>()

	fun selectTimer(selected: CustomTimer) {
		selectedTimer.value = selected
	}

	fun selectType(selected: TimerType) {
		selectedType.value = selected
	}

	fun selectById(typeId: Int) {
		val selected = typelist.value!!.filter { item -> item.typeId == typeId }
		selectedType.value = selected[0]
	}

	fun buildDatabase(context: Context) {
		db = Room.databaseBuilder(
			context,
			LocalDatabase::class.java,
			DatabaseName
		)
			.allowMainThreadQueries()
			.build()

		timerDao = db.customTimerDao()
		typeDao = db.timerTypeDao()
	}

	fun refreshTypes() {
		typelist.setValue(typeDao.getAll())
	}

	fun refreshTimers() {
		timerList.setValue(timerDao.getAll())
	}

	fun saveTimer(type: CustomTimer) {
		timerDao.insertAll(type)
	}

	fun saveType(type: TimerType) {
		typeDao.insertAll(type)
	}

	fun getTypeFromTypes(id: Int): TimerType {
		val current: List<TimerType> = typelist.value!!
		val filter = current.filter { type -> type.typeId == id }
		lateinit var returned: TimerType
		if (filter.isNotEmpty()) {
			returned = filter[0]
		}
		return returned
	}

	fun deleteTimer(current: CustomTimer) {
		timerDao.delete(current)
	}
}