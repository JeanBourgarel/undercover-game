package com.example.myundercover.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myundercover.PlayerCard
import com.example.myundercover.R
import com.example.myundercover.fragments.GameViewModel
import java.io.Serializable


class PlayerCardAdapter(val playerCards: ArrayList<PlayerCard>, val context: Context, val listener: ICardRecycler) : RecyclerView.Adapter<PlayerCardHolder>() {

    override fun getItemCount(): Int {
        return playerCards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.grid_layout_list_item, parent, false)
        return PlayerCardHolder(cellForRow, context, listener)
    }

    override fun onBindViewHolder(holder: PlayerCardHolder, position: Int) {
        val playerCard = playerCards[position]

        holder.icon.setImageResource(playerCard.icon!!)
        holder.name.text = playerCard.name
        holder.view.setOnClickListener {
            holder.listener.clickOnCard(holder)
        }
    }

    interface ICardRecycler {
        fun clickOnCard(holder: PlayerCardHolder)
    }
}

class PlayerCardHolder(val view: View, val context: Context, val listener: PlayerCardAdapter.ICardRecycler) : RecyclerView.ViewHolder(view), Serializable {
    var icon = view.findViewById<ImageView>(R.id.icon_player_card)
    var name = view.findViewById<TextView>(R.id.player_name)
}