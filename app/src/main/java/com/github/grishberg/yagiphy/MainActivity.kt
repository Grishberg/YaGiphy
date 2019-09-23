package com.github.grishberg.yagiphy

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsUseCase
import com.github.grishberg.contentdetails.gateway.ContentRepository
import com.github.grishberg.contentdetailspresentation.ContentDetailsFacade
import com.github.grishberg.giphygateway.CardsListGateway
import com.github.grishberg.giphygateway.CardsLruImageRepository
import com.github.grishberg.imagelist.ImageListUseCase
import com.github.grishberg.imageslistpresentation.ImagesListFacade
import com.github.grishberg.imageslistpresentation.VerticalCardFactory
import com.github.grishberg.main.domain.ApplicationUseCase
import com.github.grishberg.main.presentation.Router
import com.github.grishberg.yagiphy.BuildConfig.API_KEY

/**
 * Creates all modules together.
 */
class MainActivity : AppCompatActivity() {
    private val uiScope = MainScope()
    private lateinit var imagesListFacade: ImagesListFacade
    private lateinit var appUseCase: ApplicationUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(uiScope)

        val content = findViewById<ViewGroup>(R.id.content)

        val cardListInput = CardsListGateway(uiScope, API_KEY)

        val memClass = getMemoryClassFromActivity()
        val imagesGateway = CardsLruImageRepository.create(uiScope, memClass)
        val cardList = ImageListUseCase(cardListInput, imagesGateway)

        val cardFactory = VerticalCardFactory(cardList)
        cardListInput.setCardFactory(cardFactory)

        imagesListFacade = ImagesListFacade(cardList)
        imagesListFacade.attachToParent(this, content)
        cardList.requestCardsFirstPage()


        val contentDetailsInput = ContentRepository.create(uiScope)
        val contentDetails: ContentDetails =
            ContentDetailsUseCase(uiScope, imagesGateway, contentDetailsInput)

        val contentDetailsFacade = ContentDetailsFacade(contentDetails)
        appUseCase = ApplicationUseCase(cardList, contentDetails)
        contentDetailsFacade.attachToParent(this, content)

        Router(this, appUseCase, imagesListFacade, contentDetailsFacade)
    }

    private fun getMemoryClassFromActivity(): Int {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.memoryClass
    }

    override fun onBackPressed() {
        if (!appUseCase.onBackPressed()) {
            super.onBackPressed()
        }
    }
}
