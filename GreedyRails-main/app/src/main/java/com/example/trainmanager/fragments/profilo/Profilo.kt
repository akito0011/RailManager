package com.example.trainmanager.fragments.profilo

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.lifecycleScope
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentProfiloBinding
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.DeleteUserRequestDataClass
import com.example.trainmanager.fragments.percorso.RetrofitInstance
import com.example.trainmanager.persistence.LocalCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Profilo : Fragment() {

    companion object {
        fun newInstance() = Profilo()
    }

    private lateinit var binding : FragmentProfiloBinding

    private val viewModel: ProfiloViewModel by viewModels() {
        ProfiloViewModel.factory(
            (requireActivity().application as MyApplication).userRepository,
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
        binding = FragmentProfiloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), viewModel.nome, Toast.LENGTH_SHORT).show()
        binding.nome.text = "${viewModel.nome} ${viewModel.cognome}"
        binding.email.text = "${viewModel.email}"
        binding.eliminaAccountButton.setOnClickListener {
            viewModel.eliminaAccount().observe(viewLifecycleOwner) { result ->
                if (result) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Errore durante l'eliminazione dell'account, riprovare più tardi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}