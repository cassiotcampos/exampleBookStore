package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.repository.SharedP


abstract class BookListViewModelBase(
    application: Application,
    sharedP: SharedP,
) : BookBaseViewModel(application, sharedP) {

    internal val _myBooks = MutableLiveData<BookListDTO>()
    val myBooks : LiveData<BookListDTO> = _myBooks

    abstract fun loadBooks()
}