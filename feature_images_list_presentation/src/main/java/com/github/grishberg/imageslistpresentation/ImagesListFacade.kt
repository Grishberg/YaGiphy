package com.github.grishberg.imageslistpresentation

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardItemDecoration
import com.github.grishberg.imageslistpresentation.rv.CardsAdapter
import com.google.android.material.snackbar.Snackbar

private const val COLUMNS_COUNT = 2
/***
 * Facade for vertical card list view.
 */
class ImagesListFacade(
    private val cardsList: CardsList
) : LifecycleObserver {
    private var rootView: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var vm: ImagesListViewModel? = null

    /**
     * Creates vertical list view and attaches to {@param parent}
     */
    fun attachToParent(activity: FragmentActivity, parent: ViewGroup) {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(cardsList))
            .get(ImagesListViewModel::class.java)

        val adapter = CardsAdapter(LayoutInflater.from(activity))

        val lm = GridLayoutManager(activity, COLUMNS_COUNT)
        val rv = createRecyclerView(activity, adapter, lm)
        rv.addItemDecoration(CardItemDecoration(activity.resources))
        val refreshLayout = createRefreshLayout(activity).apply {
            addView(rv)
        }
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
                val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
                cardsList.onScrollStateChanged(lastVisibleItemPosition)
            }
        })
        if (viewModel.imageListState != null) {
            restoreStateIfNeeded(rv, viewModel.imageListState)
            viewModel.imageListState = null
        }
        layoutManager = lm
        rootView = refreshLayout
        vm = viewModel
    }

    private fun restoreStateIfNeeded(rv: RecyclerView, imageListState: Parcelable?) {
        imageListState?.let { lmState ->
            rv.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    fun hide() {
        rootView?.let { it.visibility = View.GONE }
    }

    fun show() {
        rootView?.let { it.visibility = View.VISIBLE }
    }

    private fun createRecyclerView(
        activity: FragmentActivity,
        adapter: CardsAdapter,
        layoutManager: LinearLayoutManager
    ): RecyclerView {
        val rv = RecyclerView(activity)
        rv.id = R.id.cardsListView
        rv.adapter = adapter
        rv.layoutManager = layoutManager
        return rv
    }

    private fun createRefreshLayout(activity: FragmentActivity): SwipeRefreshLayout {
        val refreshLayout = SwipeRefreshLayout(activity)
        refreshLayout.setOnRefreshListener {
            cardsList.requestCardsFirstPage()
        }
        return refreshLayout
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun saveState() {
        layoutManager?.let { lm ->
            val cardsLayoutManagerState = lm.onSaveInstanceState()
            vm?.let { it.imageListState = cardsLayoutManagerState }
        }
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