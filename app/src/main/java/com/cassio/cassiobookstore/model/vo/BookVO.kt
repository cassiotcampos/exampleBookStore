package com.cassio.cassiobookstore.model.vo

import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.cassio.cassiobookstore.model.dto.BookDTO

class BookVO(private val bookDTO: BookDTO) : BaseVO() {
    constructor() : this(BookDTO())

    var isFav : Boolean = false

    fun getBuyLinkFormattedSpanned(): Spanned {
        return HtmlCompat.fromHtml("<u>${getBuyLink()}</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun isBuyLinkAvailable(): Boolean {
        bookDTO.saleInfoDTO?.buyLink?.let {
            return true
        } ?: return false
    }

    fun isAuthorAvailable(): Boolean {
        bookDTO.volumeInfoDTO?.authors?.let {
            return true
        } ?: return false
    }

    fun getAuthorVisibility(): Int {
        return getVisibility(isAuthorAvailable())
    }

    fun getTitle(): String {
        bookDTO.volumeInfoDTO?.title?.let {
            return it
        } ?: return ""
    }

    fun getBookDescription(): Spanned {
        if (!bookDTO.volumeInfoDTO.description.isNullOrEmpty()) {
            return HtmlCompat.fromHtml(
                bookDTO.volumeInfoDTO.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else if (bookDTO.searchInfoDTO != null && !bookDTO.searchInfoDTO.textSnippet.isNullOrEmpty()) {
            return HtmlCompat.fromHtml(
                bookDTO.searchInfoDTO.textSnippet,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
        return HtmlCompat.fromHtml("Description not available", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun getAuthorText(): String {
        var authorsDescription = ""
        bookDTO.volumeInfoDTO?.authors?.forEach {
            authorsDescription += it + ", "
        }
        authorsDescription = authorsDescription.dropLast(2)
        authorsDescription += "."
        return authorsDescription
    }

    fun getThumbUrl(): String {
        bookDTO.volumeInfoDTO?.imageLinksDTO?.smallThumbnail?.let {
                return it.replace("http://", "https://")
        }

        bookDTO.volumeInfoDTO?.imageLinksDTO?.thumbnail?.let {
            return it.replace("http://", "https://")
        }

        return ""
    }

    fun getBuyLinkVisibility(): Int {
        return getVisibility(isBuyLinkAvailable())
    }

    fun getBuyLink(): String {
        bookDTO.saleInfoDTO?.buyLink?.let {
            return it
        } ?: return "Not available"
    }

    fun isThumbAvailable(): Boolean {
        return (bookDTO.volumeInfoDTO?.imageLinksDTO?.smallThumbnail.isNullOrEmpty().not()
                || bookDTO.volumeInfoDTO?.imageLinksDTO?.thumbnail.isNullOrEmpty().not())
    }

    fun getId(): String? {
        return bookDTO.id
    }

    fun getBookDTO(): BookDTO {
        return bookDTO
    }
}