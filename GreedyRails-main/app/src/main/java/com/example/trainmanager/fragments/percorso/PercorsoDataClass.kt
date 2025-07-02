package com.example.trainmanager.fragments.percorso

data class PercorsoDataClass(
    val tappe : List<TappaDataClass>,
    val numero_cambi : Int,
    val durata_viaggio : Int,
    val mezzi : List<String>
)
