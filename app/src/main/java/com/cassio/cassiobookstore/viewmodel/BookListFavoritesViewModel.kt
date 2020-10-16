package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.repository.SharedP


class BookListFavoritesViewModel(
    application: Application,
    private val sharedP: SharedP,
) : BookListViewModelBase(application, sharedP) {

    override fun loadBooks() {
        val booksTemp = BookListDTO()
        booksTemp.bookDTOS = sharedP.getFavorites()
        _myBooks.value = booksTemp
    }
}