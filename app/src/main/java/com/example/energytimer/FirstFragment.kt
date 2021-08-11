package com.example.energytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.energytimer.adapters.TimerItemAdapter
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.databinding.FragmentFirstBinding
import com.example.energytimer.fragment.SharedData
import com.example.energytimer.fragment.ShowTimerFragment
import com.example.energytimer.fragment.ShowTypeFragment
import com.example.energytimer.tools.DatabaseName
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
	private var _binding: FragmentFirstBinding? = null
	private val binding get() = _binding!!
	private var currentList: List<CustomTimer> = listOf()
	private lateinit var recyclerView: RecyclerView
	private lateinit var timerAdapter: TimerItemAdapter
	private val model: SharedData by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		timerAdapter = TimerItemAdapter { timer -> adapterOnClick(timer) }
		timerAdapter.submitList(currentList)
		recyclerView = binding.recyclerViewTimers
		recyclerView.adapter = timerAdapter
		model.buildDatabase(requireContext())
		recyclerView.addItemDecoration(
			DividerItemDecoration(
				this.context,
				DividerItemDecoration.VERTICAL
			)
		)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		model.timerList.observe(viewLifecycleOwner, { list ->
			timerAdapter.submitList(list)
		})
		model.refreshTimers()
		model.refreshTypes()
		binding.createTimer.setOnClickListener {
			val newFragment = ShowTimerFragment()
			newFragment.show(parentFragmentManager, "")
		}
		super.onViewCreated(view, savedInstanceState)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun adapterOnClick(customTimer: CustomTimer) {
		model.selectTimer(customTimer)
		ShowTimerFragment().show(parentFragmentManager, "TIMER")
	}

}
