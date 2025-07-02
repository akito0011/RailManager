package com.example.trainmanager.fragments.percorso

import android.R.attr.data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainmanager.R


class PercorsiAdapter(private val listaPercorsi : MutableList<PercorsoDataClass>, private val listener : OnItemClickListener, private val isItinerario : Boolean) : RecyclerView.Adapter<PercorsiAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(percorso: PercorsoDataClass)
        fun onItemLongClick(percorso: PercorsoDataClass)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val suggerimento_tv: TextView = itemView.findViewById(R.id.suggerimenti_tv)
        val orarioPartenza : TextView = itemView.findViewById(R.id.orarioArrivo)
        val dataPartenza : TextView = itemView.findViewById(R.id.dataArrivo)
        val orarioArrivo : TextView = itemView.findViewById(R.id.orarioPartenza)
        val dataArrivo : TextView = itemView.findViewById(R.id.dataPartenza)
        val tempoPercorso : TextView = itemView.findViewById(R.id.tempoPercorso)
        val numeroCambi : TextView = itemView.findViewById(R.id.numeroCambi)
        val stazionePartenza : TextView = itemView.findViewById(R.id.stazionePartenza)
        val stazioneArrivo : TextView = itemView.findViewById(R.id.stazioneArrivo)
        val logoCompagnia : ImageView = itemView.findViewById(R.id.logoCompagnia)
        val imageSlash : ImageView = itemView.findViewById(R.id.imageSlash)
        val imageTraghetto : ImageView = itemView.findViewById(R.id.imageTraghetto)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listaPercorsi[position])
                }
            }
            if (isItinerario) {
                itemView.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(listaPercorsi[position])
                    }
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.percorso_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.logoCompagnia.setImageResource(R.drawable.simbolo_treno_regionale)
        holder.orarioPartenza.text =
            listaPercorsi[position].tappe[0].orario_arrivo.substringAfter("T")
        holder.dataPartenza.text =
            listaPercorsi[position].tappe[0].orario_arrivo.substringBefore("T")
        holder.orarioArrivo.text =
            listaPercorsi[position].tappe[listaPercorsi[position].tappe.size - 1].orario_arrivo.substringAfter("T")
        holder.dataArrivo.text =
            listaPercorsi[position].tappe[listaPercorsi[position].tappe.size - 1].orario_arrivo.substringBefore("T")
        if ((listaPercorsi[position].durata_viaggio % 60) == 0) {
            holder.tempoPercorso.text =
                (listaPercorsi[position].durata_viaggio / 60).toString() + " h"
        } else {
            holder.tempoPercorso.text =
                (listaPercorsi[position].durata_viaggio / 60).toString() + " h " + (listaPercorsi[position].durata_viaggio % 60).toString() + " min"
        }
        // if numero_cambi is 0, set null string
        if (listaPercorsi[position].numero_cambi == 0) {
            holder.numeroCambi.text = ""
        } else{
            holder.numeroCambi.text = listaPercorsi[position].numero_cambi.toString() + " Cambi"
        }
        holder.stazionePartenza.text = listaPercorsi[position].tappe[0].stazione_nome
        holder.stazioneArrivo.text = listaPercorsi[position].tappe[listaPercorsi[position].tappe.size - 1].stazione_nome
        if (listaPercorsi[position].mezzi.contains("Traghetto")) {
            holder.imageTraghetto.visibility = View.VISIBLE
            holder.imageSlash.visibility = View.VISIBLE
        } else {
            holder.imageTraghetto.visibility = View.GONE
            holder.imageSlash.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listaPercorsi.size
    }

    fun updateData(newData: List<PercorsoDataClass>) {
        listaPercorsi.clear()
        listaPercorsi.addAll(newData)
        notifyDataSetChanged()
    }

    fun getData() : MutableList<PercorsoDataClass> {
        return listaPercorsi
    }

    fun removeItem(position: Int) {
        listaPercorsi.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: PercorsoDataClass, position: Int) {
        listaPercorsi.add(position, item)
        notifyItemInserted(position)
    }

}