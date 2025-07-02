package com.example.trainmanager.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.SchermataRegistrazioneBinding
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrazioneActivity : AppCompatActivity() {

    private lateinit var binding : SchermataRegistrazioneBinding

    private val viewModel: RegistrazioneViewModel by viewModels() {
        RegistrazioneViewModel.factory((this.application as MyApplication).userRepository, (this.application as MyApplication).sharedPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataRegistrazioneBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.registrationButton.setOnClickListener {
            viewModel.setNome(binding.textRegistrazioneNome.text.toString())
            viewModel.setCognome(binding.textRegistrazioneCognome.text.toString())
            viewModel.setEmail(binding.textRegistrazioneEmail.text.toString())
            viewModel.setPassword(binding.textRegistrazionePassword.text.toString())
            viewModel.registraUtente().observe(this) { success ->
                if (success) {
                    val intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Assicurarsi che tutti i campi siano stato compilati correttamente o riprovare più tardi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}