package com.github.grishberg.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.main.domain.ApplicationUseCase
import com.github.grishberg.main.domain.OutputBounds

class RouterViewModel(
    private val applicationLogic: ApplicationUseCase
) : ViewModel(), OutputBounds {
    private val _showCardContentScreen = MutableLiveData<Boolean>()
    val showCardContentScreen: LiveData<Boolean> = _showCardContentScreen

    init {
        applicationLogic.registerOutputBounds(this)
    }

    override fun showDetailedInformation() {
        _showCardContentScreen.value = true
    }

    override fun showCardsList() {
        _showCardContentScreen.value = false
    }
}