package com.example.trainmanager.fragments.percorso

import RangeTimePickerDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.activities.NotifyWorker
import com.example.trainmanager.databinding.FragmentPercorsoBinding
import com.example.trainmanager.fragments.itinerario.SharedItinerariViewModel
import com.example.trainmanager.persistence.LocalCacheManager
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit


class Percorso : Fragment() {

    private lateinit var binding: FragmentPercorsoBinding
    private lateinit var suggerimentiAdapter: SuggerimentiAdapter
    private lateinit var percorsiAdapter: PercorsiAdapter
    private lateinit var tappeAdapter: TappeAdapter
    private var isItinerario: Boolean = false
    lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel: PercorsoViewModel by viewModels() {
        PercorsoViewModel.factory((requireActivity().application as MyApplication).percorsiRepository)
    }
    private val sharedItinerariViewModel: SharedItinerariViewModel by activityViewModels() {
        SharedItinerariViewModel.factory((requireActivity().application as MyApplication).itinerariRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback = object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.recyclerSuggerimenti.adapter == tappeAdapter) {
                    binding.recyclerSuggerimenti.adapter = percorsiAdapter
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            this, backPressedCallback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentPercorsoBinding.inflate(inflater, container, false)
        val args: PercorsoArgs by navArgs()
        isItinerario = args.isItinerario
        setupRecyclerview()
        viewModel.selectedStazionePartenza.observe(viewLifecycleOwner) { stazione ->
            binding.from.setText("${stazione.nome_stazione} (${stazione.provincia}, ${stazione.citta})")
        }
        viewModel.selectedStazioneDestinazione.observe(viewLifecycleOwner) { stazione ->
            binding.to.setText("${stazione.nome_stazione} (${stazione.provincia}, ${stazione.citta})")
        }
        viewModel.selectedDataPartenza.observe(viewLifecycleOwner) { data ->
            binding.editTextDate.setText(data)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isItinerario && (sharedItinerariViewModel.percorsiSelectedItinerario.value?.size!! > 0)
        ) {
            val lastPercorso = sharedItinerariViewModel.percorsiSelectedItinerario.value?.last()
            val lastTappa = lastPercorso?.tappe?.last()!!
            viewModel.selectStazionePartenza(
                lastTappa.stazione_provincia,
                lastTappa.stazione_citta,
                lastTappa.stazione_nome
            )
            binding.from.isEnabled = false
            binding.from.isFocusable = false
            binding.from.isClickable = false
            binding.swapButton.isEnabled = false
        }

        binding.switchCambi.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                binding.switchDiretto.isChecked = false
            }
        }
        binding.switchDiretto.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                binding.switchCambi.isChecked = false
            }
        }

        binding.swapButton.setOnClickListener {
            val temp = viewModel.selectedStazionePartenza.value
            viewModel.selectStazionePartenza(
                viewModel.selectedStazioneDestinazione.value!!.provincia,
                viewModel.selectedStazioneDestinazione.value!!.citta,
                viewModel.selectedStazioneDestinazione.value!!.nome_stazione
            )
            viewModel.selectStazioneDestinazione(
                temp!!.provincia,
                temp.citta,
                temp.nome_stazione
            )
        }

        val formatterClient = SimpleDateFormat("yyyy-MM-dd HH:mm")
        binding.editTextDate.text = "${formatterClient.format(System.currentTimeMillis())}"
        val formatterServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        viewModel.selectDataPartenza(formatterServer.format(System.currentTimeMillis()))

        binding.searchPercorsoTrova.setOnClickListener {
            binding.textSuggerimenti.visibility = View.GONE
            viewModel.setPercorsi(
                binding.slider.value.toInt(),
                binding.switchCambi.isChecked,
                binding.switchDiretto.isChecked
            ).observe(viewLifecycleOwner) { success ->
                if (success) {
                    binding.recyclerSuggerimenti.adapter = percorsiAdapter
                } else {
                    Toast.makeText(
                        context,
                        "Errore nella ricerca del percorso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.from.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.textSuggerimenti.visibility = View.VISIBLE
                if (s.isNotEmpty()) {
                    viewModel.setSuggerimenti(s.toString()).observe(viewLifecycleOwner) { success ->
                        if (success) {
                            binding.recyclerSuggerimenti.adapter = suggerimentiAdapter
                        }
                    }
                }
            }
        })

        binding.to.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.textSuggerimenti.visibility = View.VISIBLE
                viewModel.setSuggerimenti(s.toString()).observe(viewLifecycleOwner) { success ->
                    if (success) {
                        binding.recyclerSuggerimenti.adapter = suggerimentiAdapter
                    } else {
                        Toast.makeText(
                            context,
                            "Errore nella ricerca dei suggerimenti!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        if (isItinerario) {
            binding.editTextDate.setOnClickListener {
                val minData =
                    sharedItinerariViewModel.percorsiSelectedItinerario.value?.last()?.tappe?.last()?.orario_arrivo?.substringBefore(
                        "T"
                    )!!
                val minTempo =
                    sharedItinerariViewModel.percorsiSelectedItinerario.value?.last()?.tappe?.last()?.orario_arrivo?.substringAfter(
                        "T"
                    )!!
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(minData)
                val minDateMillis = date.time
                pickDateTime(
                    minDateMillis,
                    minTempo.split(":")[0].toInt(),
                    minTempo.split(":")[1].toInt()
                ) {
                    viewModel.selectDataPartenza(formatterServer.format(it.time))
                }
            }
        } else {
            binding.editTextDate.setOnClickListener {
                pickDateTime(System.currentTimeMillis()) {
                    viewModel.selectDataPartenza(formatterServer.format(it.time))
                }
            }
        }

    }

    private fun setupRecyclerview() {
        binding.recyclerSuggerimenti.layoutManager = LinearLayoutManager(context)
        suggerimentiAdapter =
            SuggerimentiAdapter(mutableListOf(), object : SuggerimentiAdapter.OnItemClickListener {
                override fun onItemClick(stazione: StazioneDataClass) {
                    if (binding.root.findFocus() == binding.from) {
                        binding.to.requestFocus()
                        viewModel.selectStazionePartenza(
                            stazione.provincia,
                            stazione.citta,
                            stazione.nome_stazione
                        )
                    } else {
                        binding.from.requestFocus()
                        viewModel.selectStazioneDestinazione(
                            stazione.provincia,
                            stazione.citta,
                            stazione.nome_stazione
                        )
                    }
                }
            })
        binding.recyclerSuggerimenti.adapter = suggerimentiAdapter
        tappeAdapter = TappeAdapter(mutableListOf())
        percorsiAdapter =
            PercorsiAdapter(mutableListOf(), object : PercorsiAdapter.OnItemClickListener {
                override fun onItemClick(percorso: PercorsoDataClass) {
                    binding.recyclerSuggerimenti.adapter = tappeAdapter
                    tappeAdapter.updateData(percorso.tappe)
                }

                override fun onItemLongClick(percorso: PercorsoDataClass) {
                    val tolleranza = binding.slider.value.toInt()
                    val senza_cambi = binding.switchCambi.isChecked
                    val diretto = binding.switchDiretto.isChecked
                    sharedItinerariViewModel.aggiungiPercorsoItinerario(
                        percorso,
                        tolleranza,
                        senza_cambi,
                        diretto
                    ).observe(viewLifecycleOwner) { success ->
                        if (success) {
                            addNotification(percorso.tappe[0].orario_partenza)
                            findNavController().navigate(
                                R.id.action_percorso_to_dettagliItinerario,
                                null,
                                NavOptions.Builder().setPopUpTo(R.id.itinerario, true).build()
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Errore nell'aggiunta del percorso all'itinerario!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }, isItinerario)
        viewModel.suggerimenti.observe(viewLifecycleOwner) { suggerimenti ->
            suggerimentiAdapter.updateData(suggerimenti)
        }
        viewModel.percorsi.observe(viewLifecycleOwner) { percorsi ->
            percorsiAdapter.updateData(percorsi)
        }
    }

    private fun addNotification(dataPartenza: String) {
        createNotificationChannel()
        val workTag = "ItinerariNotification"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val localDateTime = LocalDateTime.parse(dataPartenza, formatter)
        val instant =
            localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val millis = instant.toEpochMilli()
        val differenzaMillis =
            millis - (System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1))
        val notificationWork: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                .setInitialDelay(differenzaMillis, TimeUnit.MILLISECONDS)
                .addTag(workTag)
                .build()
        WorkManager.getInstance(requireActivity()).enqueue(notificationWork)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ItinerariNotificationChannel"
            val descriptionText = "Canale usato per le notifiche degli itinerari"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("ItinerariNotificationChannel", name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun pickDateTime(
        minDate: Long = 0,
        minHour: Int = 0,
        minMinute: Int = 0,
        consumer: (Calendar) -> Unit
    ) {
        Log.d("PICKERTIME", "minHour: $minHour, minMinute: $minMinute")
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime[Calendar.YEAR]
        val startMonth = currentDateTime[Calendar.MONTH]
        val startDay = currentDateTime[Calendar.DAY_OF_MONTH]
        val startHour = currentDateTime[Calendar.HOUR_OF_DAY]
        val startMinute = currentDateTime[Calendar.MINUTE]
        val a = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val b = RangeTimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        val pickedDateTime = Calendar.getInstance()
                        val adjustedHour = if (hour < minHour) minHour else hour
                        pickedDateTime.set(year, month, day, adjustedHour, minute)
                        consumer(pickedDateTime)
                    },
                    startHour,
                    startMinute,
                    true
                )
                b.setMin(minHour, minMinute)
                b.show()
            },
            startYear,
            startMonth,
            startDay
        )
        a.datePicker.minDate = minDate
        a.show()
    }
}