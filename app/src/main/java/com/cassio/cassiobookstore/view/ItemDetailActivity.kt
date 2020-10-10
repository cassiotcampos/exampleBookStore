package com.cassio.cassiobookstore.view


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Item
import com.google.gson.Gson


class ItemDetailActivity : AppCompatActivity() {

    lateinit var item : Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        setSupportActionBar(findViewById(R.id.detail_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            item = Gson().fromJson(
                intent.getStringExtra(ItemDetailFragment.ARG_BOOK),
                Item::class.java
            )

            item.let {
                loadContent()
                replaceFragment()
            } ?: onBackPressed()

        }else {
            item = Gson().fromJson(
                savedInstanceState.getString(ItemDetailFragment.ARG_BOOK),
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
                    ItemDetailFragment.ARG_BOOK,
                    intent.getStringExtra(ItemDetailFragment.ARG_BOOK)
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit()
    }

    private fun addFragment() {
        val fragment = ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString(
                    ItemDetailFragment.ARG_BOOK,
                    intent.getStringExtra(ItemDetailFragment.ARG_BOOK)
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.item_detail_container, fragment)
            .commit()
    }

    private fun loadContent() {
        item.let {

            supportActionBar?.title = item?.volumeInfo?.title


            val myOptions = RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(this)
                .asBitmap()
                .transition(BitmapTransitionOptions.withCrossFade())
                .load(it?.volumeInfo?.imageLinks?.smallThumbnail?.replace("http://", "https://"))
                .apply(myOptions)
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        var img = findViewById<ImageView>(R.id.img_toolbar_bg)
                        transition?.let {
                            if (!transition.transition(
                                    Bitmap.createScaledBitmap(
                                        resource,
                                        2,
                                        12,
                                        true
                                    ), BitmapImageViewTarget(img)
                                )
                            )
                                img?.setImageBitmap(
                                    Bitmap.createScaledBitmap(
                                        resource,
                                        2,
                                        12,
                                        true
                                    )
                                )

                        }
                    }
                })
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
            outState.putString(ItemDetailFragment.ARG_BOOK, Gson().toJson(item))
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