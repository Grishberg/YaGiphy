package com.github.grishberg.imageslistpresentation.rv

import androidx.recyclerview.widget.DiffUtil
import com.github.grishberg.core.Card

class DiffUtilCallback(
    private val oldItems: List<Card>,
    private val newItems: List<Card>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].isContentTheSame(newItems[newItemPosition])
}