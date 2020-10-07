package com.cassio.cassiobookstore.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Item
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson

class ItemDetailFragment : Fragment() {

    private var bookStr: String? = null
    lateinit var bookMaster: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_BOOK)) {
                bookStr = it.getString(ARG_BOOK)
                bookMaster = Gson().fromJson(bookStr, Item::class.java)

                // ACTIONBAR TITLE
                var toolBar = activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
                toolBar?.title = bookMaster.volumeInfo.title
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // TITLE
        bookMaster.volumeInfo?.title?.let {
            rootView.findViewById<TextView>(R.id.detail_title).text = it
        }

        // THUMB IMG
            bookMaster.volumeInfo?.imageLinks?.smallThumbnail?.let {
                val myOptions = RequestOptions()
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)

                Glide.with(this)
                    .load(Uri.parse(it))
                    .apply(myOptions)
                    .into(rootView.findViewById(R.id.img_book_detail))
            }

        // AUTHORS
        if (!bookMaster.volumeInfo?.authors.isNullOrEmpty()) {
            var authorsDescription = ""
            bookMaster.volumeInfo?.authors?.forEach {
                authorsDescription += it + ", "
            }
            authorsDescription = authorsDescription.dropLast(2)
            authorsDescription += "."

            rootView.findViewById<View>(R.id.container_details_authors).visibility = View.VISIBLE
            rootView.findViewById<TextView>(R.id.detail_authors).text = authorsDescription
        }


        // DESCRIPTION
        if (!bookMaster.volumeInfo.description.isNullOrEmpty()) {
            rootView.findViewById<TextView>(R.id.item_detail).text =
                bookMaster.volumeInfo.description
        } else if (bookMaster.searchInfo != null && !bookMaster.searchInfo.textSnippet.isNullOrEmpty()) {
            rootView.findViewById<TextView>(R.id.item_detail).text =
                HtmlCompat.fromHtml(
                    bookMaster.searchInfo.textSnippet,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
        }

        // BUY LINK
        bookMaster.saleInfo?.buyLink?.let { itString ->
            rootView.findViewById<LinearLayout>(R.id.container_book_buy_link).visibility = View.VISIBLE
            val myLink = HtmlCompat.fromHtml("<u>$itString</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            rootView.findViewById<TextView>(R.id.detail_buylink).text = myLink
            rootView.findViewById<TextView>(R.id.detail_buylink).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(itString)
                startActivity(intent)
            }
        }
        return rootView
    }

    companion object {
        const val ARG_BOOK = "book"
    }
}