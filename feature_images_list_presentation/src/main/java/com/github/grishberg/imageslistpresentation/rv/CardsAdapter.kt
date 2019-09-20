package com.github.grishberg.imageslistpresentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.grishberg.core.Card
import com.github.grishberg.imageslistpresentation.R
import com.github.grishberg.imageslistpresentation.VerticalListCard

internal class CardsAdapter(
    private val inflater: LayoutInflater
) : RecyclerView.Adapter<CardViewHolder>() {
    private val items = mutableListOf<Card<*>>()

    fun populate(cards: List<Card<*>>) {
        DiffUtil.calculateDiff(DiffUtilCallback(items, cards))
            .dispatchUpdatesTo(this)
        items.clear()
        items.addAll(cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = inflater.inflate(R.layout.card_item, parent, false)
        val vh = CardViewHolder(view)
        view.setOnClickListener {
            val pos = vh.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                //TODO: handle click
            }
        }
        return vh
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        (items[position] as VerticalListCard).render(holder)
    }

    override fun getItemCount(): Int = items.size
}