package com.example.energytimer

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.energytimer.adapters.TimerItemAdapter
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.databinding.FragmentFirstBinding
import com.example.energytimer.tools.DatabaseName
import com.example.energytimer.tools.Help
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random.Default.nextInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

	private var _binding: FragmentFirstBinding? = null
	private val binding get() = _binding!!
	private lateinit var db: LocalDatabase
	private var currentList: List<CustomTimer> = listOf()
	private lateinit var recyclerView: RecyclerView
	private lateinit var timerAdapter: TimerItemAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		timerAdapter = TimerItemAdapter { timer -> adapterOnClick(timer) }
		timerAdapter.submitList(currentList)
		recyclerView = binding.recyclerViewTimers
		recyclerView.adapter = timerAdapter
		return binding.root
	}

	fun insert() = runBlocking {
		thread {
			db = Room.databaseBuilder(
				requireContext(),
				LocalDatabase::class.java,
				DatabaseName
			).build()
			val timerDao = db.customTimerDao()
			val currentList = timerDao.getAll()
			timerAdapter.submitList(currentList)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		insert()
		binding.createTimer.setOnClickListener { view ->
			val myToast = Toast.makeText(context,"toast message with gravity",Toast.LENGTH_SHORT)
			myToast.setGravity(Gravity.LEFT,200,200)
			myToast.show()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun adapterOnClick(flower: CustomTimer) {
		Help.printLog("fragment-1", flower.toString())
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
	}

	private fun getData(): List<CustomTimer> {
		val currentDate: Long = Date().time
		val maxValue = 160
		val currentTic = 480
		val customList: MutableCollection<CustomTimer> = mutableListOf()
		val randomValues = List(3) { nextInt(0, maxValue) }
		for (i in randomValues) {
			val finishDate = currentDate + ((maxValue - i) * currentTic * 1000)
			Help.printLog(
				"Factory",
				"Timer $i | " +
						"${Date(finishDate)} $finishDate ${(maxValue - i) * currentTic * 1000} |" +
						"${(finishDate - currentDate) / currentTic}"
			)
			customList.add(
				CustomTimer(
					i,
					1,
					"resin-$i",
					"des-$i",
					i,
					maxValue, currentTic,
					currentDate,
					finishDate
				)
			)
		}
		return customList.toList()
	}
}