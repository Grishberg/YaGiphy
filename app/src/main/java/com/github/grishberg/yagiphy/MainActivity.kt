package com.github.grishberg.yagiphy

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsUseCase
import com.github.grishberg.contentdetails.gateway.ContentRepository
import com.github.grishberg.contentdetailspresentation.ContentDetailsFacade
import com.github.grishberg.giphygateway.CardsListGateway
import com.github.grishberg.giphygateway.CardsLruImageRepository
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imagelist.CardListUseCase
import com.github.grishberg.imageslist.CardsList
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
    private lateinit var contentDetails: ContentDetails
    private lateinit var cardList: CardsList
    private lateinit var appUseCase: ApplicationUseCase
    private lateinit var imagesListFacade: ImagesListFacade

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(uiScope)

        if (savedInstanceState == null) {
            create()
        } else {
            restore()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkDeepLink(appUseCase, intent)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return UseCaseStorage(cardList, contentDetails, appUseCase)
    }

    override fun onBackPressed() {
        if (!appUseCase.onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun create() {
        val giphyApi = GiphyApi(API_KEY)

        val cardListInput = CardsListGateway(uiScope, giphyApi)

        val memClass = getMemoryClassFromActivity()
        val imagesGateway = CardsLruImageRepository.create(uiScope, memClass)
        cardList = CardListUseCase(uiScope, cardListInput, imagesGateway)

        val cardFactory = VerticalCardFactory(cardList)
        cardListInput.setCardFactory(cardFactory)

        val contentDetailsInput = ContentRepository.create(uiScope, giphyApi)
        contentDetails = ContentDetailsUseCase(uiScope, imagesGateway, contentDetailsInput)

        appUseCase = ApplicationUseCase(cardList, contentDetails)

        createViews(cardList, contentDetails, appUseCase)
        checkDeepLink(appUseCase, intent)
    }

    private fun checkDeepLink(
        appUseCase: ApplicationUseCase,
        intent: Intent?
    ) {
        if (intent == null) {
            return
        }
        val action = intent.action
        val data = intent.dataString

        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            val cardId = data.substring(data.lastIndexOf("/") + 1)
            appUseCase.onRequestedByDeepLink(cardId)
        }
    }
    private fun getMemoryClassFromActivity(): Int {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.memoryClass
    }

    private fun restore() {
        val configuration = getLastCustomNonConfigurationInstance()
        if (configuration is UseCaseStorage) {
            contentDetails = configuration.contentDetails
            cardList = configuration.cardList
            appUseCase = configuration.appUseCase

            createViews(cardList, contentDetails, appUseCase)
        }
    }

    private fun createViews(
        cardList: CardsList,
        contentDetails: ContentDetails,
        appUseCase: ApplicationUseCase
    ) {
        val content = findViewById<ViewGroup>(R.id.content)

        imagesListFacade = ImagesListFacade(cardList)
        imagesListFacade.attachToParent(this, content)
        lifecycle.addObserver(imagesListFacade)

        val contentDetailsFacade = ContentDetailsFacade(contentDetails)
        contentDetailsFacade.attachToParent(this, content)

        Router(this, appUseCase, imagesListFacade, contentDetailsFacade)
    }

    private data class UseCaseStorage(
        val cardList: CardsList,
        val contentDetails: ContentDetails,
        val appUseCase: ApplicationUseCase
    )
}
