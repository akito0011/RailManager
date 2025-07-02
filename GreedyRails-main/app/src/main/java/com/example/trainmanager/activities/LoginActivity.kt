package com.example.trainmanager.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.trainmanager.MyApplication
import com.example.trainmanager.R
import com.example.trainmanager.databinding.SchermataLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: SchermataLoginBinding

    private val viewModel: LoginViewModel by viewModels() {
        LoginViewModel.factory(
            (this.application as MyApplication).userRepository,
            (this.application as MyApplication).itinerariRepository,
            (this.application as MyApplication).sharedPreferences
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (viewModel.checkIfUserIsLoggedIn()) {
            viewModel.syncLocalCache()
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }

        binding.textRegistration.setOnClickListener {
            val intent = Intent(this, RegistrazioneActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            viewModel.setEmail(binding.textLoginEmail.text.toString())
            viewModel.setPassword(binding.textLoginPassword.text.toString())
            viewModel.login().observe(this) { result ->
                if (result) {
                    val intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Email o password errati, riprovare!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}