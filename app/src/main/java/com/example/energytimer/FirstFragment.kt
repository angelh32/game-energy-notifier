package com.example.energytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.energytimer.adapters.TimerItemAdapter
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.databinding.FragmentFirstBinding
import com.example.energytimer.tools.Help
import java.util.*
import kotlin.random.Random.Default.nextInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

	private var _binding: FragmentFirstBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		val timerAdapter = TimerItemAdapter { timer -> adapterOnClick(timer) }
		val currentList: List<CustomTimer> = getData()
		timerAdapter.submitList(currentList)
		val recyclerView: RecyclerView = binding.recyclerView
		recyclerView.adapter = timerAdapter
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.buttonFirst.setOnClickListener {
			findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun adapterOnClick(flower: CustomTimer) {
		Help.printLog("fragment-1",flower.toString())
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