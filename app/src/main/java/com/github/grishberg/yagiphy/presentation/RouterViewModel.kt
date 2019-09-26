package com.github.grishberg.yagiphy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.yagiphy.domain.ApplicationUseCase
import com.github.grishberg.yagiphy.domain.AppUseCaseOutput

/**
 * Sends messages from {@link ApplicationUseCase} to router.
 */
class RouterViewModel(
    applicationLogic: ApplicationUseCase
) : ViewModel(), AppUseCaseOutput {
    private val _showCardContentScreen = MutableLiveData<Boolean>()
    val showCardContentScreen: LiveData<Boolean> = _showCardContentScreen

    init {
        applicationLogic.registerOutput(this)
    }

    override fun showDetailedInformation() {
        _showCardContentScreen.value = true
    }

    override fun showCardsList() {
        _showCardContentScreen.value = false
    }
}