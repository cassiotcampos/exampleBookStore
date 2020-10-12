package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.repository.BooksApi
import com.cassio.cassiobookstore.repository.SharedP
import com.cassio.cassiobookstore.view.util.Utils
import com.cassio.cassiobookstore.viewmodel.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookListViewModel(
    application: Application,
    sharedP: SharedP,
    private val booksApi: BooksApi,
) : BookBaseViewModel(application, sharedP) {

    private var apiIndex: Int = 0
    private val maxResults = 40
    val isFav: Boolean = false

    val showError = SingleLiveEvent<String>()
    val showLoading: ObservableBoolean = ObservableBoolean(false)

    val lastBooksLoaded = MutableLiveData<Books>()
    var allBooksLoaded: Books = Books()

    init {
        allBooksLoaded.items = arrayListOf()
    }

    fun loadBooks() {
        showLoading.set(true)
        if (!isFav) loadFromApi() else loadFavoritesFromSharedP()
    }

    private fun loadFavoritesFromSharedP() {
        lastBooksLoaded.value = loadFavorites()
        allBooksLoaded = lastBooksLoaded.value!!
    }

    private fun loadFromApi() {

        booksApi
            .getBooks(
                "android",
                maxResults,
                apiIndex,
                callbackGetBooks()
            )
    }

    private fun callbackGetBooks(): Callback<Books>? {
        return (object : Callback<Books> {
            override fun onResponse(call: Call<Books>?, response: Response<Books>?) {
                onResponseSuccessBooks(response)
            }

            override fun onFailure(call: Call<Books>?, t: Throwable?) {
                onResponseFailureBooks()
            }
        })
    }

    private fun onResponseFailureBooks() {
        showError.postValue("Api error")
    }

    private fun onResponseSuccessBooks(response: Response<Books>?) {

        if (response != null) {

            val tempResult: Books = response.body()!!

            if (tempResult.items != null) {

                apiIndex += maxResults

                if (allBooksLoaded?.items.isNullOrEmpty()) {
                    allBooksLoaded = tempResult
                } else {
                    allBooksLoaded?.items?.addAll(tempResult.items)
                }

                // notify UI
                lastBooksLoaded.postValue(tempResult)
            }
        }
    }


    companion object {

        @JvmStatic
        @BindingAdapter("loadImgUrl")
        fun loadImageThumb(imgView: ImageView, url: String?) {

            if (url != null) {
                loadThumbFromUrl(imgView, url)
            } else {
                loadThumbDefault(imgView)
            }
        }

        @JvmStatic
        @BindingAdapter("loadImageBg")
        fun loadImageBg(imgView: ImageView, url: String?) {

            if (url != null) {
                loadBackgroundFromUrl(imgView, url)
            } else {
                loadBackgroundDefault(imgView)
            }
        }

        fun loadThumbDefault(imgView: ImageView) {
            imgView.setImageDrawable(
                ContextCompat.getDrawable(
                    imgView.context,
                    R.drawable.ic_no_image_black
                )
            )

            imgView.setBackgroundColor(
                ContextCompat.getColor(
                    imgView.context,
                    R.color.colorPrimaryDark
                )
            )
        }

        fun loadBackgroundDefault(imgView: ImageView) {
            imgView.setImageDrawable(
                ContextCompat.getDrawable(
                    imgView.context,
                    R.color.colorPrimaryDark
                )
            )
        }

        fun loadBackgroundFromUrl(imgBg: ImageView, url: String) {

            Utils.loadBlurriedImg(imgBg.context,
                url.replace("http://", "https://"),
            imgBg)
        }

        fun loadThumbFromUrl(imgView: ImageView, url: String) {

            imgView.setBackgroundColor(ContextCompat.getColor(imgView.context, R.color.transparent))

            val myOptions = RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(imgView.rootView.context) // at least the row context for correct centerCrop
                .load(url.replace("http://", "https://"))
                .apply(myOptions)
                .into(imgView)
        }
    }
}