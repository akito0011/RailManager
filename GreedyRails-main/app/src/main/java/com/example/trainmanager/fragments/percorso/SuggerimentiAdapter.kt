package com.example.trainmanager.fragments.percorso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainmanager.R

class SuggerimentiAdapter(private val listaStazioni : MutableList<StazioneDataClass>, private val listener : OnItemClickListener) : RecyclerView.Adapter<SuggerimentiAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(stazione: StazioneDataClass)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val suggerimento_tv: TextView = itemView.findViewById(R.id.suggerimenti_tv)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listaStazioni[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.suggerimenti_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.suggerimento_tv.text = listaStazioni[position].nome_stazione + " (" + listaStazioni[position].provincia + ", " + listaStazioni[position].citta + ")"
    }

    override fun getItemCount(): Int {
        return listaStazioni.size
    }

    fun updateData(newData: List<StazioneDataClass>) {
        listaStazioni.clear()
        listaStazioni.addAll(newData)
        notifyDataSetChanged()
    }

}