package com.example.energytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.energytimer.adapters.TimerItemAdapter
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.databinding.FragmentFirstBinding
import com.example.energytimer.fragment.ShowTimerFragment
import com.example.energytimer.tools.DatabaseName
import com.example.energytimer.tools.Help
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


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
	val DIALOG_FRAGMENT = 1

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		timerAdapter = TimerItemAdapter { timer -> adapterOnClick(timer) }
		timerAdapter.submitList(currentList)
		recyclerView = binding.recyclerViewTimers
		recyclerView.adapter = timerAdapter
		recyclerView.addItemDecoration(
			DividerItemDecoration(
				this.context,
				DividerItemDecoration.VERTICAL
			)
		)
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
			val newFragment = ShowTimerFragment()
			newFragment.show(parentFragmentManager, "")
		}
	}

	override fun onDestroyView() {
		db.close()
		super.onDestroyView()
		_binding = null
	}

	private fun adapterOnClick(flower: CustomTimer) {
		Help.printLog("fragment-1", flower.toString())
		
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
	}

}