package com.example.trainmanager.persistence.percorsi

import com.example.trainmanager.fragments.percorso.PercorsoRichiestaDataClass
import com.example.trainmanager.fragments.trova.RichiestaPercorsiStazioneDataClass

class PercorsiRemoteDataSource(private val percorsiAPI: PercorsiAPI) {

    suspend fun getPercorso(
        stazionePartenzaProvincia: String,
        stazionePartenzaCitta: String,
        stazionePartenzaNomeStazione: String,
        stazioneDestinazioneProvincia: String,
        stazioneDestinazioneCitta: String,
        stazioneDestinazioneNomeStazione: String,
        orarioPartenza: String,
        tolleranza: Int,
        senzaCambi: Boolean,
        diretto: Boolean
    ) = percorsiAPI.getPercorso(
        PercorsoRichiestaDataClass(
            stazionePartenzaProvincia,
            stazionePartenzaCitta,
            stazionePartenzaNomeStazione,
            stazioneDestinazioneProvincia,
            stazioneDestinazioneCitta,
            stazioneDestinazioneNomeStazione,
            orarioPartenza,
            tolleranza,
            senzaCambi,
            diretto
        )
    )

    suspend fun getStazioni(nomeStazioneRegex: String) = percorsiAPI.getStazioni(nomeStazioneRegex)

    suspend fun getPercorsiStazione(
        stazioneProvincia: String,
        stazioneCitta: String,
        stazioneNome: String
    ) = percorsiAPI.percorsiStazione(
        RichiestaPercorsiStazioneDataClass(stazioneProvincia, stazioneCitta, stazioneNome)
    )

}