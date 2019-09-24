package com.github.grishberg.imageslistpresentation.rv

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.grishberg.imageslistpresentation.R

class CardItemDecoration(
    resources: Resources
) : RecyclerView.ItemDecoration() {
    private val margin = resources.getDimensionPixelOffset(R.dimen.card_grid_margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        // hide the divider for the last child
        if (position > 1) {
            outRect.set(margin, 0, margin, margin)
        } else {
            outRect.set(margin, margin, margin, margin)
        }
    }
}