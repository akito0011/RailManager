package com.example.trainmanager.persistence

import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import kotlinx.coroutines.flow.Flow

class ItinerariLocalDataSource(private val itinerariDao : ItinerariDao) {
        fun getItinerari() : Flow<List<ItinerarioDataClass>> = itinerariDao.getItinerari()

        //fun getPercorsiItinerario(nomeItinerario : String) : Flow<String> = itinerariDao.getPercorsiItinerario(nomeItinerario)

        suspend fun getPercorsiItinerario(nomeItinerario : String) : String = itinerariDao.getPercorsiItinerario(nomeItinerario)

        suspend fun insertItinerario(itinerario: ItinerarioEntity) = itinerariDao.insertItinerario(itinerario)

        suspend fun deleteItinerario(nomeItinerario : String) = itinerariDao.deleteItinerario(nomeItinerario)

        suspend fun updatePercorsoItinerario(nomeItinerario : String, percorsoJson : String) = itinerariDao.updatePercorsoItinerario(nomeItinerario, percorsoJson)

        suspend fun deleteAllItinerari() = itinerariDao.deleteAllItinerari()
}