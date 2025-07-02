package com.example.trainmanager.fragments.itinerario

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentItinerarioBinding
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.RetrofitInstance
import com.example.trainmanager.persistence.LocalCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Itinerario : Fragment() {

    companion object {
        fun newInstance() = Itinerario()
    }

    private lateinit var binding : FragmentItinerarioBinding

    private val viewModel: SharedItinerariViewModel by activityViewModels() {
        SharedItinerariViewModel.factory((requireActivity().application as MyApplication).itinerariRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItinerarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.addItinerario.setOnClickListener {
            findNavController().navigate(R.id.action_itinerario_to_creazioneItinerario, null, NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build())
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerItinerari.layoutManager = LinearLayoutManager(context)
        val itinerariAdapter = ItinerariAdapter(mutableListOf(), object : ItinerariAdapter.OnItemClickListener {
            override fun onItemClick(itinerario: ItinerarioDataClass) {
                viewModel.selectItinerario(itinerario)
                findNavController().navigate(R.id.action_itinerario_to_dettagliItinerario, null, NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build())
            }
        })
        binding.recyclerItinerari.adapter = itinerariAdapter
        viewModel.allItinerari.observe(viewLifecycleOwner) { itinerari ->
            itinerariAdapter.updateData(itinerari)
        }
    }

}