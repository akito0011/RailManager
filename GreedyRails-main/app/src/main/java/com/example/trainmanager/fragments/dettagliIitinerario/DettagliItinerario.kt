package com.example.trainmanager.fragments.dettagliIitinerario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentDettagliItinerarioBinding
import com.example.trainmanager.databinding.FragmentPercorsoBinding
import com.example.trainmanager.fragments.itinerario.SharedItinerariViewModel
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.PercorsiAdapter
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.fragments.percorso.TappeAdapter
import com.journaldev.androidrecyclerviewswipetodelete.SwipeToDeleteCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DettagliItinerario : Fragment() {

    companion object {
        fun newInstance() = DettagliItinerario()
    }

    private val viewModel: SharedItinerariViewModel by activityViewModels() {
        SharedItinerariViewModel.factory((requireActivity().application as MyApplication).itinerariRepository)
    }

    private lateinit var binding : FragmentDettagliItinerarioBinding
    private lateinit var percorsiAdapter : PercorsiAdapter
    private lateinit var tappeAdapter : TappeAdapter
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback =       object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.recyclerItinerari.adapter == tappeAdapter) {
                    binding.recyclerItinerari.adapter = percorsiAdapter
                } else {
                    findNavController().navigate(R.id.action_dettagliItinerario_to_itinerario)
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            this,backPressedCallback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDettagliItinerarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.titoloItinerario.text = viewModel.selectedItinerario.value?.nome_itinerario
        binding.addPercorso.setOnClickListener() {
            val action = DettagliItinerarioDirections.actionDettagliItinerarioToPercorso(true)
            findNavController().navigate(action, NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build())
            }
        binding.eliminaItinerario.setOnClickListener() {
            viewModel.deleteItinerario().observe(viewLifecycleOwner) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Itinerario eliminato con successo!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_dettagliItinerario_to_itinerario, null, NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build())
                } else {
                    Toast.makeText(requireContext(), "Errore nell'eliminazione dell'itinerario!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }

    private fun setupRecyclerView() {
        binding.recyclerItinerari.layoutManager = LinearLayoutManager(context)
        percorsiAdapter =
            PercorsiAdapter(mutableListOf(), object : PercorsiAdapter.OnItemClickListener {
                override fun onItemClick(percorso: PercorsoDataClass) {
                    binding.recyclerItinerari.adapter = tappeAdapter
                    tappeAdapter.updateData(percorso.tappe)
                }
                override fun onItemLongClick(percorso: PercorsoDataClass) {
                    // do nothing
                }
            }, false)
        binding.recyclerItinerari.adapter = percorsiAdapter
        tappeAdapter = TappeAdapter(mutableListOf())
        enableSwipeToDeleteAndUndo()
        viewModel.percorsiSelectedItinerario.observe(viewLifecycleOwner) { listaPercorsi ->
            percorsiAdapter.updateData(listaPercorsi)
        }
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val isLastPosition = position == percorsiAdapter.itemCount - 1
                if (isLastPosition) {
                    viewModel.eliminaUltimoPercorsoItinerario().observe(viewLifecycleOwner) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Percorso eliminato con successo!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Errore nell'eliminazione del percorso!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "È possibile eliminare soltanto l'ultimo percorso di un itinerario!", Toast.LENGTH_LONG).show()
                    percorsiAdapter.notifyItemChanged(position)
                }
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(binding.recyclerItinerari)
    }
}