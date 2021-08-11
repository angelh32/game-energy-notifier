package com.example.energytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.energytimer.adapters.TypeItemAdapter
import com.example.energytimer.database.TimerType
import com.example.energytimer.databinding.FragmentSecondBinding
import com.example.energytimer.fragment.SharedData
import com.example.energytimer.fragment.ShowTypeFragment

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
	private val model: SharedData by activityViewModels()

	private var _binding: FragmentSecondBinding? = null
	private val binding get() = _binding!!
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
		model.buildDatabase(requireContext())
		recyclerView.addItemDecoration(
			DividerItemDecoration(
				this.context,
				DividerItemDecoration.VERTICAL
			)
		)
		return binding.root
	}

	private fun adapterOnClick(timerType: TimerType) {
		model.selectType(timerType)
		ShowTypeFragment().show(parentFragmentManager, "TYPE")
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		model.typelist.observe(viewLifecycleOwner, { list ->
			typeItemAdapter.submitList(list)
		})
		model.refreshTypes()
		binding.createType.setOnClickListener {
			val newFragment = ShowTypeFragment()
			newFragment.show(parentFragmentManager, "")
		}
		super.onViewCreated(view, savedInstanceState)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}