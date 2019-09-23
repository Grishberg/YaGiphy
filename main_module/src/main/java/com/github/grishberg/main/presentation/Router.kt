package com.github.grishberg.main.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.github.grishberg.imageslist.CardListPresentationFacade


class Router(
    private val activity: FragmentActivity,
    private val viewModel: RouterViewModel,
    private val cardsListFacade: CardListPresentationFacade
) {

    init {
        viewModel.showCardContentScreen.observe(activity, Observer<Boolean> { shouldShow ->
            if (shouldShow) {
                showContentDetailsScreen()

            } else {
                showCardsList()
            }
        })
    }

    private fun showContentDetailsScreen() {
    }

    private fun showCardsList() {
    }
}