package com.example.trainmanager.fragments.itinerario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainmanager.R

class ItinerariAdapter(private val listaItinerari : MutableList<ItinerarioDataClass>, private val listener : OnItemClickListener) : RecyclerView.Adapter<ItinerariAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(percorso: ItinerarioDataClass)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeItinerario : TextView = itemView.findViewById(R.id.nomeItinerario)
        val numeroPercorsi : TextView = itemView.findViewById(R.id.numeroPercorsi)
        val dataInizio : TextView = itemView.findViewById(R.id.dataInizio)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listaItinerari[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itinerario_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeItinerario.text = listaItinerari[position].nome_itinerario
        holder.numeroPercorsi.text = if (listaItinerari[position].numero_percorsi > 0) (listaItinerari[position].numero_percorsi + 1).toString() + " Tappe" else ""
        holder.dataInizio.text = listaItinerari[position].data_inizio
    }

    override fun getItemCount(): Int {
        return listaItinerari.size
    }

    fun updateData(newData: List<ItinerarioDataClass>) {
        listaItinerari.clear()
        listaItinerari.addAll(newData)
        notifyDataSetChanged()
    }

}