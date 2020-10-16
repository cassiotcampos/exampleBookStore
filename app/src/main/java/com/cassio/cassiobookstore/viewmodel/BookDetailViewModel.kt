package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import com.cassio.cassiobookstore.model.dto.BookDTO
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.repository.SharedP
import com.cassio.cassiobookstore.viewmodel.util.SingleLiveEvent


class BookDetailViewModel(
    application: Application,
    private val sharedP: SharedP,
) : BookBaseViewModel(application, sharedP) , IBookDetailViewModel {

    val bookVO =
        MutableLiveData<BookVO>().apply { value = BookVO(BookDTO()) }


    val onClickBuyLink = SingleLiveEvent<String>()
    val isFav = MutableLiveData<Boolean>()

    fun setBookDetail(mBook: BookVO) {
        bookVO.value = mBook
        isFav.value = sharedP.isFav(bookVO.value!!.getBookDTO())
        isFav.postValue(isFav.value)
    }

    override fun onClickBuyLink() {

        if (bookVO.value!!.isBuyLinkAvailable()) {
            onClickBuyLink.postValue(bookVO.value!!.getBuyLink())
        }
    }

    override fun getThumbUrl(): String {
        return bookVO.value!!.getThumbUrl()
    }

    override fun getBookDescription(): Spanned {
        return bookVO.value!!.getBookDescription()
    }

    override fun onClickFav() {
        val newFavValue = isFav.value?.not()
        if (newFavValue != null) {
            sharedP.addOrRemoveFav(newFavValue, bookVO.value!!.getBookDTO())
            isFav.postValue(newFavValue)
        }
    }

    override fun getTitle(): String {
        return bookVO.value!!.getTitle()
    }

    override fun getAuthorVisibility() : Int {
        return bookVO.value!!.getAuthorVisibility()
    }

    override fun getAuthorText(): String {
        return bookVO.value!!.getAuthorText()
    }

    override fun getBuyLinkVisibility() : Int {
        return bookVO.value!!.getBuyLinkVisibility()
    }

    override fun getBuyLink(): Spanned {
        return bookVO.value!!.getBuyLinkFormattedSpanned()
    }
}