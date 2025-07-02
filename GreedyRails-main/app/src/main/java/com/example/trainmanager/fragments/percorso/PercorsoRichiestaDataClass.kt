package com.example.trainmanager.fragments.percorso

data class PercorsoRichiestaDataClass(
    var stazione_partenza_provincia : String,
    var stazione_partenza_citta : String,
    var stazione_partenza_nome_stazione : String,
    var stazione_destinazione_provincia : String,
    var stazione_destinazione_citta : String,
    var stazione_destinazione_nome_stazione : String,
    var orario_partenza : String,
    var tolleranza : Int,
    var senza_cambi : Boolean,
    var diretto : Boolean
)
