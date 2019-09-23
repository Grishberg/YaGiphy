package com.github.grishberg.contentdetails

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity

interface ContentDetailsPresentationFacade {
    fun attachToParent(activity: FragmentActivity, parent: ViewGroup)

    fun show()

    fun hide()
}