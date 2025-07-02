package com.example.trainmanager.fragments.home

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentHomeBinding
import com.example.trainmanager.fragments.itinerario.SharedItinerariViewModel
import com.example.trainmanager.persistence.LocalCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Home : Fragment() {

    companion object {
        fun newInstance() = Home()
    }

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by activityViewModels() {
        HomeViewModel.factory(
            (requireActivity().application as MyApplication).itinerariRepository,
            (requireActivity().application as MyApplication).sharedPreferences
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.benvenutoTextView.text = "Benvenuto, ${viewModel.nome}!"
        binding.buttonCreaItinerario.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_to_creazioneItinerario,
                null,
                NavOptions.Builder().setPopUpTo(R.id.home, true).build()
            )
        }
        binding.buttonLogout.setOnClickListener {
            viewModel.logout().observe(viewLifecycleOwner) {
                requireActivity().finish()
            }
        }
    }
}