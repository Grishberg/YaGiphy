package com.github.grishberg.imageslist

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity

interface CardListPresentationFacade {
    fun attachToParent(activity: FragmentActivity, parent: ViewGroup)
}