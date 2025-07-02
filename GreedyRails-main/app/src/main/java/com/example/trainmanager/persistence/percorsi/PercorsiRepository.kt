package com.example.trainmanager.persistence.percorsi

class PercorsiRepository(private val percorsiRemoteDataSource : PercorsiRemoteDataSource) {

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
    ) = percorsiRemoteDataSource.getPercorso(
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

    suspend fun getStazioni(nomeStazioneRegex: String) = percorsiRemoteDataSource.getStazioni(nomeStazioneRegex)

    suspend fun getPercorsiStazione(
        stazioneProvincia: String,
        stazioneCitta: String,
        stazioneNome: String
    ) = percorsiRemoteDataSource.getPercorsiStazione(
        stazioneProvincia,
        stazioneCitta,
        stazioneNome
    )

}