package com.github.grishberg.main.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.grishberg.contentdetails.ContentDetailsPresentationFacade
import com.github.grishberg.imageslist.CardListPresentationFacade
import com.github.grishberg.main.domain.ApplicationUseCase


class Router(
    private val activity: FragmentActivity,
    private val appUseCase: ApplicationUseCase,
    private val cardsListFacade: CardListPresentationFacade,
    private val contentDetailsFacade: ContentDetailsPresentationFacade
) {

    init {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(appUseCase))
            .get(RouterViewModel::class.java)


        viewModel.showCardContentScreen.observe(activity, Observer<Boolean> { shouldShow ->
            if (shouldShow) {
                showContentDetailsScreen()

            } else {
                showCardsList()
            }
        })
    }

    private fun showContentDetailsScreen() {
        cardsListFacade.hide()
        contentDetailsFacade.show()
    }

    private fun showCardsList() {
        contentDetailsFacade.hide()
        cardsListFacade.show()
    }

    @Suppress("UNCHECKED_CAST")
    private class ViewModelFactory(
        private val appUseCase: ApplicationUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RouterViewModel(appUseCase) as T
        }
    }
}