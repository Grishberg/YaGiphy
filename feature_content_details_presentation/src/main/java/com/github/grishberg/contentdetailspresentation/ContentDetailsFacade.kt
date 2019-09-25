package com.github.grishberg.contentdetailspresentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.core.CardInfo
import com.github.grishberg.core.ImageHolder
import com.google.android.material.snackbar.Snackbar

/**
 * Facade for content details presentation layer.
 */
class ContentDetailsFacade(
    private val contentDetails: ContentDetails
) {
    private var view: View? = null

    fun attachToParent(activity: FragmentActivity, parent: ViewGroup) {
        val viewModel = ViewModelProviders
            .of(activity, ViewModelFactory(contentDetails))
            .get(ContentDetailsViewModel::class.java)

        val inflater = LayoutInflater.from(activity)
        val rootView = inflater.inflate(R.layout.content_details_layout, parent, false)

        val imageView = rootView.findViewById<ImageView>(R.id.contentDetailsImage)
        val twitterHashTagView = rootView.findViewById<TextView>(R.id.twitterHashTag)
        val userNameView = rootView.findViewById<TextView>(R.id.userName)
        val fullNameView = rootView.findViewById<TextView>(R.id.fullUserName)
        val renderer =
            GiphyContentRenderer(imageView, twitterHashTagView, userNameView, fullNameView)

        viewModel.showError.observe(activity, Observer<String> { message ->
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
        })

        viewModel.content.observe(activity, Observer<CardInfo> { cardInfo ->
            renderer.hideTwitterHashTag()
            cardInfo.render(renderer)
        })

        viewModel.twitterHashTag.observe(activity, Observer<TwitterHashTag> { twitterHashTag ->
            renderer.showTwitterHashTitle(twitterHashTag.name)
        })

        viewModel.needInvalidateImage.observe(activity, Observer<ImageHolder> { image ->
            renderer.showTargetBitmap(image)
        })

        viewModel.showStub.observe(activity, Observer<Boolean> { shouldShowStub ->
            if (shouldShowStub) {
                renderer.showDefaultBackground()
            }
        })

        userNameView.setOnClickListener {
            contentDetails.onUserNameClicked()
        }

        view = rootView
        parent.addView(rootView)
    }

    fun show() {
        view?.let { v ->
            v.visibility = View.VISIBLE
        }
    }

    fun hide() {
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