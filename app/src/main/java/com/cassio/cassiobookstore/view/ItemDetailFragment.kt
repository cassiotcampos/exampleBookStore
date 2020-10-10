package com.cassio.cassiobookstore.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.repository.loadFavsFromShared
import com.cassio.cassiobookstore.repository.saveFavsIntoShared
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ItemDetailFragment : Fragment() {

    private lateinit var myFavoriteBooks: Books
    private var bookStr: String? = null
    lateinit var bookMaster: Item
    var isFavorite: Boolean = false

    private lateinit var btnFav: Button

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
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(this)
                .load(Uri.parse(it.replace("http://", "https://")))
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
            rootView.findViewById<LinearLayout>(R.id.container_book_buy_link).visibility =
                View.VISIBLE
            val myLink = HtmlCompat.fromHtml("<u>$itString</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            rootView.findViewById<TextView>(R.id.detail_buylink).text = myLink
            rootView.findViewById<TextView>(R.id.detail_buylink).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(itString.replace("http://", "https://"))
                startActivity(intent)
            }
        }


        // FAVORITE
        btnFav = rootView.findViewById<Button>(R.id.btn_adicionar_favorito)
        myFavoriteBooks = loadFavsFromShared(context!!)
        myFavoriteBooks?.items?.forEach meuLoop@{
            if (bookMaster.id.equals(it.id)) {
                changeFavBtn(true)
                return@meuLoop
            }
        }

        if (!isFavorite) {
            changeFavBtn(false)
        }

        btnFav.setOnClickListener {
            if (!isFavorite && (addToFavorites(rootView))) {
                setActivityResultUnfavorited(false)
            } else if (isFavorite && removeFavorite(rootView)) {
                setActivityResultUnfavorited(true) // refresh the list after unfavorite
            }
        }



        return rootView
    }

    // refresh the list removing the unfavorited title
    // only for portrait flow
    private fun setActivityResultUnfavorited(unfavorited: Boolean) {
        if (isPortrait()) {

            val act: ItemDetailActivity = activity as ItemDetailActivity

            if (unfavorited) {
                act.setActivityResultItemUnfavorited()
            } else {
                act.setActivityResultCanceled()
            }
        }
    }

    private fun isPortrait(): Boolean {
        return activity is ItemDetailActivity
    }

    private fun changeFavBtn(isFav: Boolean) {
        this.isFavorite = isFav
        if (isFavorite) {
            btnFav.text = activity?.getText(R.string.remove_favorite)
            btnFav.setBackgroundColor(activity?.resources?.getColor(R.color.colorRed)!!)
            btnFav.setTextColor(activity?.resources?.getColor(R.color.white)!!)
        } else {
            btnFav.text = activity?.getText(R.string.add_favorite)
            btnFav.setBackgroundColor(activity?.resources?.getColor(R.color.grayDark)!!)
            btnFav.setTextColor(activity?.resources?.getColor(R.color.white)!!)
        }
    }

    private fun removeFavorite(viewForSnackBar: View): Boolean {
        myFavoriteBooks.items.forEach meuLoop@{
            if (it.id.equals(bookMaster.id)) {
                myFavoriteBooks.items.remove(it)
                context?.let { itContext -> saveFavsIntoShared(itContext, myFavoriteBooks) }
                mensagemRemovido(viewForSnackBar)
                if (activity is ItemListActivity) {
                    (activity as ItemListActivity).favRemoved(bookMaster)
                }
                return true

            }
        }
        return false
    }

    fun addToFavorites(viewForSnackBar: View): Boolean {
        var myFavs = loadFavsFromShared(context!!)
        // verifica se ja existe
        myFavs?.items?.forEach() meuLoop@{
            if (it.id.equals(bookMaster.id)) {
                mensagemAdicionado(viewForSnackBar)
                if (activity is ItemListActivity) {
                    (activity as ItemListActivity).favRemoved(bookMaster)
                }
                return true
            }
        }

        myFavoriteBooks.items.add(bookMaster)
        context?.let {
            if (saveFavsIntoShared(it, myFavoriteBooks)) {
                mensagemAdicionado(viewForSnackBar)
                if (activity is ItemListActivity) {
                    (activity as ItemListActivity).favReAdded(bookMaster)
                }
                return true
            }
        }
        return false
    }

    private fun mensagemAdicionado(viewForSnackBar: View) {
        changeFavBtn(true)
        Snackbar.make(
            viewForSnackBar,
            getString(R.string.msg_fav_added),
            Snackbar.LENGTH_LONG
        ).setAction("DESFAZER") {
            removeFavorite(viewForSnackBar)
        }.show();
    }

    private fun mensagemRemovido(viewForSnackBar: View) {
        changeFavBtn(false)
        Snackbar.make(
            viewForSnackBar,
            getString(R.string.msg_removed_fav),
            Snackbar.LENGTH_LONG
        ).setAction("DESFAZER") {
            addToFavorites(viewForSnackBar)
        }.show();
    }

    companion object {
        const val RESULT_ITEM_ID_UNFAVORITED: String = "RESULT_UNFAVORITED_ID"
        const val REQUEST_VIEW_FAVORITE: Int = 1
        const val ARG_BOOK = "book"
    }
}