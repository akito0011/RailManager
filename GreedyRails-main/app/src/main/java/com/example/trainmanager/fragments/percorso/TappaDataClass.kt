package com.example.trainmanager.fragments.percorso

data class TappaDataClass(
    val stazione_provincia : String,
    val stazione_citta : String,
    val stazione_nome : String,
    val orario_arrivo : String,
    val orario_partenza : String,
    val cod_mezzo : String,
    val compagnia_mezzo : String,
    val tipologia_mezzo : String
)
