package com.example.trainmanager.fragments.creazioneIitinerario

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.FragmentCreazioneItinerarioBinding
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.SharedItinerariViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class CreazioneItinerario : Fragment() {

    private lateinit var binding : FragmentCreazioneItinerarioBinding

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
        binding = FragmentCreazioneItinerarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        binding.datePicker.text = "${formatter.format(System.currentTimeMillis())}"
        binding.datePicker.setOnClickListener {
            pickDate(System.currentTimeMillis()) {
                binding.datePicker.text = "${formatter.format(it.time)}"
            }
        }
        binding.completaCreazioneItinerario.setOnClickListener(){
            val nomeItinerario = binding.editTextNomeItinerario.text.toString()
            val dataInizio = binding.datePicker.text.toString()
            val notifiche = binding.switchNotifications.isChecked
            viewModel.insertItinerario(ItinerarioDataClass( nomeItinerario, dataInizio, 0,if(notifiche) 1 else 0)).observe(viewLifecycleOwner) { success ->
                if (success) {
                    findNavController().navigate(R.id.action_creazioneItinerario_to_dettagliItinerario, null, NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build())
                } else {
                    Toast.makeText(requireContext(), "Errore nella creazione dell'itinerario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickDate(minDate: Long = 0, consumer : (Calendar) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime[Calendar.YEAR]
        val startMonth = currentDateTime[Calendar.MONTH]
        val startDay = currentDateTime[Calendar.DAY_OF_MONTH]
        val a = DatePickerDialog(requireContext(), { _, year, month, day ->
            val selectedDateTime = Calendar.getInstance()
            selectedDateTime[year, month] = day
            consumer(selectedDateTime)
        }, startYear, startMonth, startDay)
        a.datePicker.minDate = minDate
        a.show()
    }
}