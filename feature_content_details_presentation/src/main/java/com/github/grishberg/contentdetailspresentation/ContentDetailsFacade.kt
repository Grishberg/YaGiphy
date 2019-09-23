package com.github.grishberg.contentdetailspresentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsPresentationFacade
import com.google.android.material.snackbar.Snackbar

class ContentDetailsFacade(
    private val contentDetails: ContentDetails
) : ContentDetailsPresentationFacade {
    private var view: View? = null

    override fun attachToParent(activity: FragmentActivity, parent: ViewGroup) {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(contentDetails))
            .get(ContentDetailsViewModel::class.java)

        val inflater = LayoutInflater.from(activity)
        val rootView = inflater.inflate(R.layout.content_details_layout, parent, false)

        viewModel.showError.observe(activity, Observer<String> { message ->
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
        })

        view = rootView
        parent.addView(rootView)
    }

    override fun show() {
        view?.let { v ->
            v.visibility = View.VISIBLE
        }
    }

    override fun hide() {
        view?.let { v ->
            v.visibility = View.GONE
        }
    }

    @Suppress("UNCHECKED_CAST")
    private class ViewModelFactory(
        private val contentDetails: ContentDetails
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContentDetailsViewModel(contentDetails) as T
        }
    }
}