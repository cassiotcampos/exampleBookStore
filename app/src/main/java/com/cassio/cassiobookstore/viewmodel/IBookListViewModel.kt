package com.cassio.cassiobookstore.viewmodel

import android.text.Spanned


interface IBookListViewModel {

    // those methods are called directly from views.xml as property
    // ex: android:text="{bookViewModel.title}"
    // this is dataBinding
    fun onClickBuyLink()
    fun getTitle(): String
    fun getAuthorVisibility() : Int
    fun getAuthorText(): String
    fun getBuyLinkVisibility() : Int
    fun getBuyLink(): Spanned
    fun getThumbUrl(): String
    fun getBookDescription(): Spanned
}