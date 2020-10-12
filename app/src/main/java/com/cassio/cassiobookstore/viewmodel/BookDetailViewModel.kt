package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.repository.SharedP


class BookDetailViewModel(
    application: Application,
    sharedP: SharedP,
) : BookBaseViewModel(application, sharedP) {

    var bookDetail: Item = Item()

    fun getTitle() : String {
        bookDetail.volumeInfo?.title?.let {
            return it
        } ?: return ""
    }

    fun getThumbUrl() : String?{
        return bookDetail.volumeInfo?.imageLinks?.smallThumbnail
    }

    fun getAuthorVisibility() : Int {
        if(isAuthorAvailable()) return View.VISIBLE
        return View.GONE
    }

    fun getAuthorText(): String {
        var authorsDescription = ""
        bookDetail.volumeInfo?.authors?.forEach {
            authorsDescription += it + ", "
        }
        authorsDescription = authorsDescription.dropLast(2)
        authorsDescription += "."
        return authorsDescription
    }

    fun getBookDescription(): Spanned {
        if (!bookDetail.volumeInfo.description.isNullOrEmpty()) {
            return HtmlCompat.fromHtml(
                bookDetail.volumeInfo.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else if (bookDetail.searchInfo != null && !bookDetail.searchInfo.textSnippet.isNullOrEmpty()) {
            return HtmlCompat.fromHtml(
                bookDetail.searchInfo.textSnippet,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
        return HtmlCompat.fromHtml("Description not available", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun getBuyLinkVisibility() : Int{
        return getVisibility(isBuyLinkAvailable())
    }

    private fun getVisibility(isAvailable: Boolean): Int {
        if(isAvailable) return View.VISIBLE
        return View.GONE
    }

    fun getBuyLink(): Spanned {
        if(isBuyLinkAvailable()) {
            val buyLink = bookDetail.saleInfo?.buyLink
            return HtmlCompat.fromHtml("<u>$buyLink</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }else{
            return HtmlCompat.fromHtml("Not available", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun isBuyLinkAvailable(): Boolean {
        bookDetail.saleInfo?.buyLink?.let {
            return true
        } ?: return false
    }

    private fun isAuthorAvailable(): Boolean {
        return !bookDetail.volumeInfo?.authors.isNullOrEmpty()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("loadImageThumb")
        fun loadImageThumb(imgView: ImageView, url: String?) {

            if (url != null) {
                BookListViewModel.loadThumbFromUrl(imgView, url)
            } else {
                BookListViewModel.loadThumbDefault(imgView)
            }
        }
    }

    /*fun onClickBuyLink(){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(buyLink.replace("http://", "https://"))
        startActivity(intent)
    }*/


}