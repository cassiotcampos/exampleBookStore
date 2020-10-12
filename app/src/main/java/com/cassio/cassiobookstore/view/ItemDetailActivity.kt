package com.cassio.cassiobookstore.view


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.view.util.Utils
import com.google.gson.Gson


class ItemDetailActivity : AppCompatActivity() {

    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        setSupportActionBar(findViewById(R.id.detail_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            item = Gson().fromJson(
                intent.getStringExtra(ItemDetailFragment.ARG_BOOK_DETAIL),
                Item::class.java
            )

            item.let {
                loadContent()
                replaceFragment()
            } ?: onBackPressed()

        } else {
            item = Gson().fromJson(
                savedInstanceState.getString(ItemDetailFragment.ARG_BOOK_DETAIL),
                Item::class.java
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

    private fun addFragment() {
        val fragment = ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString(
                    ItemDetailFragment.ARG_BOOK_DETAIL,
                    intent.getStringExtra(ItemDetailFragment.ARG_BOOK_DETAIL)
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.item_detail_container_two_panel, fragment)
            .commit()
    }

    private fun loadContent() {
        item.let {
            supportActionBar?.title = item?.volumeInfo?.title
            Utils.loadBlurriedImg(this,
                it?.volumeInfo?.imageLinks?.smallThumbnail?.replace("http://", "https://"),
                findViewById<ImageView>(R.id.img_toolbar_bg))
        }
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
        item.volumeInfo?.let {
            outState.putString(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(item))
        }

    }

    fun setActivityResultItemUnfavorited() {
        val data = Intent()
        data.putExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED, item.id)
        setResult(RESULT_OK, data)
    }

    fun setActivityResultCanceled() {
        val data = Intent()
        data.removeExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED)
        setResult(RESULT_CANCELED, data)
    }
}