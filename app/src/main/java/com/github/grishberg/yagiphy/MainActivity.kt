package com.github.grishberg.yagiphy

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.grishberg.giphygateway.CardsListGateway
import com.github.grishberg.imagelist.ImageListUseCase
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.ImagesListFacade
import com.github.grishberg.imageslistpresentation.VerticalCardFactory
import com.github.grishberg.yagiphy.BuildConfig.API_KEY

class MainActivity : AppCompatActivity() {
    private val uiScope = MainScope()
    private lateinit var imagesListFacade: ImagesListFacade
    private lateinit var cardList: CardsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(uiScope)

        val content = findViewById<ViewGroup>(R.id.content)

        val cardFactory = VerticalCardFactory()
        val cardListInput = CardsListGateway(uiScope, cardFactory, API_KEY)
        cardList = ImageListUseCase(cardListInput)
        imagesListFacade = ImagesListFacade(cardList)
        imagesListFacade.attachToParent(this, content)
        cardList.requestCardsFirstPage()
    }
}
