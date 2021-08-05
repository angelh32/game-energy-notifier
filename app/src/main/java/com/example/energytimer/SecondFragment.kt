package com.example.energytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.energytimer.adapters.TypeItemAdapter
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.database.TimerType
import com.example.energytimer.databinding.FragmentSecondBinding
import com.example.energytimer.tools.DatabaseName
import com.example.energytimer.tools.Help
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

	private var _binding: FragmentSecondBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!
	private lateinit var db: LocalDatabase
	private var currentList: List<TimerType> = listOf()
	private lateinit var recyclerView: RecyclerView
	private lateinit var typeItemAdapter: TypeItemAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSecondBinding.inflate(inflater, container, false)
		typeItemAdapter = TypeItemAdapter { timer -> adapterOnClick(timer) }
		typeItemAdapter.submitList(currentList)
		recyclerView = binding.recyclerViewTypes
		recyclerView.adapter = typeItemAdapter
		recyclerView.addItemDecoration(
			DividerItemDecoration(
				this.context,
				DividerItemDecoration.VERTICAL
			)
		)
		return binding.root
	}

	private fun adapterOnClick(flower: TimerType) {
		Help.printLog("fragment-1", flower.toString())
//        val intent = Intent(this, FlowerDetailActivity()::class.java)
//        intent.putExtra(FLOWER_ID, flower.id)
//        startActivity(intent)
	}

	fun insert_types() = runBlocking {
		thread {
			db = Room.databaseBuilder(
				requireContext(),
				LocalDatabase::class.java,
				DatabaseName
			).build()
			val timerDao = db.timerTypeDao()
			val currentList = timerDao.getAll()
			typeItemAdapter.submitList(currentList)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		insert_types()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}