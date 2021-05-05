package com.example.myundercover.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myundercover.PlayerCard
import com.example.myundercover.R

class PlayerCardAdapter(val playerCards: ArrayList<PlayerCard>, val context: Context) : RecyclerView.Adapter<PlayerCardHolder>() {

    override fun getItemCount(): Int {
        return playerCards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.grid_layout_list_item, parent, false)
        return PlayerCardHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: PlayerCardHolder, position: Int) {
        val playerCard = playerCards[position]


        holder.icon.setImageResource(playerCard.icon!!)
        holder.name.text = playerCard.name
    }


}

class PlayerCardHolder(val view: View): RecyclerView.ViewHolder(view) {
    var icon = itemView.findViewById<ImageView>(R.id.icon_player_card)
    var name = itemView.findViewById<TextView>(R.id.player_name)
}