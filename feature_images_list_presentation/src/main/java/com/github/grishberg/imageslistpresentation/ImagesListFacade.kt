package com.github.grishberg.imageslistpresentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardsAdapter

class ImagesListFacade(
    private val cardsList: CardsList
) {
    fun attachToParent(activity: FragmentActivity, parent: ViewGroup) {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(cardsList))
            .get(ImagesListViewModel::class.java)

        val adapter = CardsAdapter(LayoutInflater.from(activity))
        val rv = RecyclerView(activity)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        parent.addView(rv)

        viewModel.cards.observe(activity, Observer<List<Card<*>>> { cards ->
            adapter.populate(cards)
        })
    }

    @SuppressWarnings("unchecked")
    private class ViewModelFactory(
        private val cardsList: CardsList
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ImagesListViewModel(cardsList) as T
        }
    }
}