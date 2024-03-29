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
import com.github.grishberg.contentdetailspresentation.CardInfoFactoryImpl
import com.github.grishberg.contentdetailspresentation.ContentDetailsFacade
import com.github.grishberg.core.CoroutineDispatchers
import com.github.grishberg.giphygateway.CardsListGateway
import com.github.grishberg.giphygateway.CardsLruImageRepository
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imagelist.CardListUseCase
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.ImagesListFacade
import com.github.grishberg.imageslistpresentation.VerticalCardFactory
import com.github.grishberg.yagiphy.BuildConfig.API_KEY
import com.github.grishberg.yagiphy.domain.ApplicationUseCase
import com.github.grishberg.yagiphy.presentation.Router
import com.github.grishberg.yagiphy.presentation.UserProfileScreenDelegate
import kotlinx.coroutines.Dispatchers


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

        if (savedInstanceState == null) {
            create()
        } else {
            restore()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val intentData = extractIntentDataOrNull(intent)

        appUseCase.handleNewIntent(intentData)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return UseCaseStorage(cardList, contentDetails, appUseCase)
    }

    private fun create() {
        val memClass = getMemoryClassFromActivity()
        val coroutineContextProvider = CoroutineDispatchersImpl()
        val imagesGateway = CardsLruImageRepository.create(uiScope, coroutineContextProvider, memClass)

        val cardFactory = VerticalCardFactory(imagesGateway, CardInfoFactoryImpl())
        val giphyApi = GiphyApi(API_KEY, cardFactory)
        val cardListInput = CardsListGateway(uiScope, coroutineContextProvider, giphyApi)

        cardList = CardListUseCase(uiScope, cardListInput, imagesGateway)

        val contentDetailsInput =
            ContentRepository.create(uiScope, coroutineContextProvider, giphyApi)
        contentDetails = ContentDetailsUseCase(
            uiScope,
            coroutineContextProvider,
            imagesGateway,
            contentDetailsInput
        )

        contentDetails.registerUserProfileOutput(UserProfileScreenDelegate(this.applicationContext))

        appUseCase =
            ApplicationUseCase(cardList, contentDetails)

        createViews(cardList, contentDetails, appUseCase)

        appUseCase.start(extractIntentDataOrNull(intent))
    }

    private fun extractIntentDataOrNull(intent: Intent?): String? {
        if (intent == null) {
            return null
        }
        val action = intent.action
        val data = intent.dataString

        if (Intent.ACTION_VIEW == action && data != null) {
            return data
        }
        return null
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

        Router(
            this,
            appUseCase,
            imagesListFacade,
            contentDetailsFacade
        )
    }

    override fun onBackPressed() {
        if (!appUseCase.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            uiScope.destroy()
        }
    }

    private data class UseCaseStorage(
        val cardList: CardsList,
        val contentDetails: ContentDetails,
        val appUseCase: ApplicationUseCase
    )

    private class CoroutineDispatchersImpl : CoroutineDispatchers {
        override val main = Dispatchers.Main
        override val io = Dispatchers.IO
    }
}
