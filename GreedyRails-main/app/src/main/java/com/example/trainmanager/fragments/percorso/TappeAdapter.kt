package com.example.trainmanager.fragments.percorso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainmanager.R

class TappeAdapter(private val listaTappe : MutableList<TappaDataClass>) : RecyclerView.Adapter<TappeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val suggerimento_tv: TextView = itemView.findViewById(R.id.suggerimenti_tv)
        val nomeStazione : TextView = itemView.findViewById(R.id.textTappa)
        val typeTransport : ImageView = itemView.findViewById(R.id.typeTransport)
        val idMezzo : TextView = itemView.findViewById(R.id.transportId)
        val orarioPartenza : TextView = itemView.findViewById(R.id.orarioPartenza)
        val dataPartenza : TextView = itemView.findViewById(R.id.dataPartenza)
        val orarioArrivo : TextView = itemView.findViewById(R.id.orarioArrivo)
        val dataArrivo : TextView = itemView.findViewById(R.id.dataArrivo)
        val arrivo : TextView = itemView.findViewById(R.id.arrivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dettagli_percorso_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.arrivo.text = "Arrivo in stazione"
        }
        holder.nomeStazione.text = listaTappe[position].stazione_nome
        holder.orarioArrivo.text = listaTappe[position].orario_arrivo.substringAfter("T")
        holder.dataArrivo.text = listaTappe[position].orario_arrivo.substringBefore("T")
        // l'ultimo elemento non ha data/ora di partenza e mezzo
        if (position == listaTappe.size - 1) {
            holder.orarioPartenza.text = ""
            holder.dataPartenza.text = ""
            holder.idMezzo.text = ""
            holder.typeTransport.visibility = View.INVISIBLE
        } else {
            holder.idMezzo.text = listaTappe[position].cod_mezzo
            holder.orarioPartenza.text = listaTappe[position].orario_partenza.substringAfter("T")
            holder.dataPartenza.text = listaTappe[position].orario_partenza.substringBefore("T")
            if (listaTappe[position].tipologia_mezzo == ("Traghetto")) {
                holder.typeTransport.setImageResource(R.drawable.boat)
            } else {
                holder.typeTransport.setImageResource(R.drawable.simbolo_treno_regionale)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaTappe.size
    }

    fun updateData(newData: List<TappaDataClass>) {
        listaTappe.clear()
        listaTappe.addAll(newData)
        notifyDataSetChanged()
    }

}