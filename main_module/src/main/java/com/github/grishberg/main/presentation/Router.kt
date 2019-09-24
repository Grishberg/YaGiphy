package com.github.grishberg.main.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.github.grishberg.contentdetails.ContentDetailsPresentationFacade
import com.github.grishberg.imageslist.CardListPresentationFacade
import com.github.grishberg.main.domain.ApplicationUseCase

/**
 * Switches cards list or content visibility.
 */
class Router(
    activity: FragmentActivity,
    appUseCase: ApplicationUseCase,
    private val cardsListFacade: CardListPresentationFacade,
    private val contentDetailsFacade: ContentDetailsPresentationFacade
) : LifecycleObserver {

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