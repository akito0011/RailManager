package com.example.trainmanager.fragments.trova

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentTrovaBinding
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.PercorsiAdapter
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoRichiestaDataClass
import com.example.trainmanager.fragments.percorso.RetrofitInstance
import com.example.trainmanager.fragments.percorso.StazioneDataClass
import com.example.trainmanager.fragments.percorso.SuggerimentiAdapter
import com.example.trainmanager.fragments.profilo.ProfiloViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Trova : Fragment() {

    private lateinit var binding: FragmentTrovaBinding
    private lateinit var suggerimentiAdapter: SuggerimentiAdapter
    private lateinit var percorsiAdapter: PercorsiAdapter

    private val viewModel: TrovaViewModel by viewModels() {
        TrovaViewModel.factory(
            (requireActivity().application as MyApplication).percorsiRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrovaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.selectedStazionePartenza.observe(viewLifecycleOwner) { stazione ->
            binding.stazione.setText("${stazione.nome_stazione} (${stazione.provincia}, ${stazione.citta})")
        }
        binding.stazione.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.textSuggerimentiTrova.visibility = View.VISIBLE
                if (s.isNotEmpty()) {
                    viewModel.setSuggerimenti(s.toString()).observe(viewLifecycleOwner) { success ->
                        if (success) {
                            binding.recyclerSuggerimenti.adapter = suggerimentiAdapter
                        }
                    }
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                // do nothing
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerSuggerimenti.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        suggerimentiAdapter =
            SuggerimentiAdapter(mutableListOf(), object : SuggerimentiAdapter.OnItemClickListener {
                override fun onItemClick(stazione: StazioneDataClass) {
                    viewModel.selectStazionePartenza(
                        stazione.provincia,
                        stazione.citta,
                        stazione.nome_stazione
                    ).observe(viewLifecycleOwner) { success ->
                        if (success) {
                            binding.textSuggerimentiTrova.visibility = View.GONE
                            binding.recyclerSuggerimenti.adapter = percorsiAdapter
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Errore nella ricerca dei percorsi, riprovare più tardi!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        binding.recyclerSuggerimenti.adapter = suggerimentiAdapter
        percorsiAdapter =
            PercorsiAdapter(mutableListOf(), object : PercorsiAdapter.OnItemClickListener {
                override fun onItemClick(percorso: PercorsoDataClass) {
                    // do nothing
                }

                override fun onItemLongClick(percorso: PercorsoDataClass) {
                    // do nothing
                }
            }, false)
        viewModel.suggerimenti.observe(viewLifecycleOwner) {
            suggerimentiAdapter.updateData(it)
        }
        viewModel.percorsi.observe(viewLifecycleOwner) {
            percorsiAdapter.updateData(it)
        }
    }
}