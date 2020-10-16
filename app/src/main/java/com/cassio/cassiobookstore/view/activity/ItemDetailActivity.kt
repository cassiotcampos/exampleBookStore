package com.cassio.cassiobookstore.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Priority
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.view.ImageUtils
import com.cassio.cassiobookstore.view.fragment.ItemDetailFragment
import com.google.gson.Gson


class ItemDetailActivity : AppCompatActivity() {

    var bookVO: BookVO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        setSupportActionBar(findViewById(R.id.detail_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            bookVO = Gson().fromJson(
                intent.getStringExtra(ItemDetailFragment.ARG_BOOK_DETAIL),
                BookVO::class.java
            )

            bookVO?.let {
                loadContent()
                replaceFragment()
            } ?: onBackPressed()

        } else {
            bookVO = Gson().fromJson(
                savedInstanceState.getString(ItemDetailFragment.ARG_BOOK_DETAIL),
                BookVO::class.java
            )
            loadContent()
            replaceFragment()
        }
    }

    private fun replaceFragment() {
        val fragment = ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString(
                    ItemDetailFragment.ARG_BOOK_DETAIL,
                    intent.getStringExtra(ItemDetailFragment.ARG_BOOK_DETAIL)
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.item_detail_container_handphone, fragment)
            .commit()
    }

    private fun loadContent() {

        supportActionBar?.title = bookVO?.getTitle()

        ImageUtils.loadImgFromUrl(
            this,
            bookVO?.getThumbUrl()!!,
            findViewById<ImageView>(R.id.img_toolbar_bg),
            Priority.IMMEDIATE,
            withBlurryEffect = true
        )

    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bookVO?.let {
            outState.putString(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(bookVO))
        }

    }

    fun setActivityResultItemUnfavorited() {
        val data = Intent()
        data.putExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED, bookVO?.getId())
        setResult(RESULT_OK, data)
    }

    fun setActivityResultCanceled() {
        val data = Intent()
        data.removeExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED)
        setResult(RESULT_CANCELED, data)
    }
}