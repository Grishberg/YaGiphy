package com.github.grishberg.imageslistpresentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardListPresentationFacade
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardsAdapter
import com.google.android.material.snackbar.Snackbar

/***
 * Facade for vertical card list view.
 */
class ImagesListFacade(
    private val cardsList: CardsList
) : CardListPresentationFacade {
    private var rootView: View? = null

    /**
     * Creates vertical list view and attaches to {@param parent}
     */
    override fun attachToParent(activity: FragmentActivity, parent: ViewGroup) {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(cardsList))
            .get(ImagesListViewModel::class.java)

        val adapter = CardsAdapter(LayoutInflater.from(activity))
        val refreshLayout = createRefreshLayout(activity)

        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val rv = createRecyclerView(activity, adapter, layoutManager, refreshLayout)
        parent.addView(refreshLayout)

        viewModel.cards.observe(activity, Observer<List<Card>> { cards ->
            adapter.populate(cards)
            refreshLayout.isRefreshing = false
        })

        viewModel.updatedCardPosition.observe(activity, Observer<Int> { updatedItemPosition ->
            adapter.notifyItemChanged(updatedItemPosition)
        })

        viewModel.onError.observe(activity, Observer<String> { message ->
            refreshLayout.isRefreshing = false
            Snackbar.make(refreshLayout, message, Snackbar.LENGTH_LONG).show()
        })

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                cardsList.onScrollStateChanged(lastVisibleItemPosition)
            }
        })
        rootView = refreshLayout
    }

    override fun hide() {
        rootView?.let { it.visibility = View.GONE }
    }

    override fun show() {
        rootView?.let { it.visibility = View.VISIBLE }
    }

    private fun createRecyclerView(
        activity: FragmentActivity,
        adapter: CardsAdapter,
        layoutManager: LinearLayoutManager,
        refreshLayout: SwipeRefreshLayout
    ): RecyclerView {
        val rv = RecyclerView(activity)
        rv.adapter = adapter
        rv.layoutManager = layoutManager
        refreshLayout.addView(rv)
        return rv
    }

    private fun createRefreshLayout(activity: FragmentActivity): SwipeRefreshLayout {
        val refreshLayout = SwipeRefreshLayout(activity)
        refreshLayout.setOnRefreshListener {
            cardsList.requestCardsFirstPage()
        }
        return refreshLayout
    }

    @Suppress("UNCHECKED_CAST")
    private class ViewModelFactory(
        private val cardsList: CardsList
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ImagesListViewModel(cardsList) as T
        }
    }
}