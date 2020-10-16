package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.repository.BooksApi
import com.cassio.cassiobookstore.repository.SharedP
import com.cassio.cassiobookstore.viewmodel.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookListViewModel(
    application: Application,
    private val sharedP: SharedP,
    private val booksApi: BooksApi,
) : BookListViewModelBase(application, sharedP) {

    private var apiIndex: Int = 0
    private val maxResults = 10

    private val _lastLoadedBooks = MutableLiveData<BookListDTO>()
    val lastLoadedBooks : LiveData<BookListDTO> = _lastLoadedBooks

    init {
        loadBooks()
    }

    val showError = SingleLiveEvent<String>()
    val hasFavorites = MutableLiveData<Boolean>()


    override fun loadBooks() {

        booksApi
            .getBooks(
                "android",
                maxResults,
                apiIndex,
                callbackGetBooks()
            )
    }

    private fun callbackGetBooks(): Callback<BookListDTO>? {
        return (object : Callback<BookListDTO> {
            override fun onResponse(call: Call<BookListDTO>?, response: Response<BookListDTO>?) {
                onResponseSuccessBooks(response)
            }

            override fun onFailure(call: Call<BookListDTO>?, t: Throwable?) {
                onResponseFailureBooks()
            }
        })
    }

    private fun onResponseFailureBooks() {
        showError.postValue("Api error")
    }

    private fun onResponseSuccessBooks(response: Response<BookListDTO>?) {

        if (response != null) {

            var tempResult: BookListDTO? = response.body()

            if (tempResult?.bookDTOS.isNullOrEmpty().not()) {

                apiIndex += maxResults

                _lastLoadedBooks.value = tempResult

                if (_myBooks.value == null) {
                    _myBooks.value = tempResult
                } else {
                    _myBooks.value?.bookDTOS?.addAll(tempResult!!.bookDTOS)
                }
            }
        }
    }

    fun verifyIfHasFavorites() {
        if (hasFavorites.value != sharedP.hasFavorites()) {
            hasFavorites.value = sharedP.hasFavorites()
        }
    }
}
