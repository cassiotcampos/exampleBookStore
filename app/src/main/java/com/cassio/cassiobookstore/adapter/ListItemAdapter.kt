package com.cassio.cassiobookstore.adapter


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cassio.cassiobookstore.ItemDetailActivity
import com.cassio.cassiobookstore.ItemDetailFragment
import com.cassio.cassiobookstore.ItemListActivity
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.adapter.ListItemAdapter.BookViewHolder
import com.cassio.cassiobookstore.model.generated.java.Books
import com.cassio.cassiobookstore.model.generated.java.Item
import com.google.gson.Gson
import jp.wasabeef.blurry.Blurry

class ListItemAdapter(
    val parentActivity: ItemListActivity,
    private var values: Books,
    private val twoPane: Boolean,
    private val onLastItemLoaded: LastItemLoadedListener,
) : RecyclerView.Adapter<BookViewHolder>() {


    interface LastItemLoadedListener {
        fun onLastItemLoaded()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parentActivity)
            .inflate(R.layout.item_list_content, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {


        val item = values.items[position]


        item.volumeInfo?.imageLinks?.smallThumbnail?.let {
            holder.updateWithUrl(it)
        }

        holder.title.text = item.volumeInfo.title

        holder.containerButton.setOnClickListener {
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_BOOK, Gson().toJson(item))
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(parentActivity, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_BOOK, Gson().toJson(item))
                }
                parentActivity.startActivity(intent)
            }
        }


        if (position == values.items.size - 1) {
            onLastItemLoaded.onLastItemLoaded()
        }
    }

    override fun getItemCount() = values.items.size

    fun addAll(mItems: ArrayList<Item>) {
        for (i in 0..mItems.size - 1) {
            if (values.items.add(mItems[i])) {
                notifyItemChanged(values.items.size)
            }
        }
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.books_title)
        var imgmini: ImageView = view.findViewById(R.id.imgmini)
        var imgbg: ImageView = view.findViewById(R.id.imgbg)
        var containerButton: LinearLayout = view.findViewById(R.id.container_book_button)


        @UiThread
        fun updateWithUrl(url: String) {


            if (url.isNotBlank()) {

                val myOptions = RequestOptions()
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                Glide.with(parentActivity)
                    .load(Uri.parse(url))
                    .apply(myOptions)
                    .into(imgmini)

                /*Glide.with(parentActivity)  //2
                    .load(Uri.parse(url)) //3
                    .apply(myOptions)
                    .apply(bitmapTransform(BlurTransformation(25, 60)))
                    .centerCrop()
                    .into(imgbg) //8*/

                Glide.with(parentActivity)
                    .asBitmap()
                    .load(url)
                    .into(object : SimpleTarget<Bitmap?>() {


                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            // como e async as vezes a activity nao esta nula na primeira verificacao mas esta nula aqui
                            if (parentActivity != null) {
                                Blurry.with(parentActivity)
                                    .radius(20)
                                    .color(
                                        parentActivity.getResources()
                                            .getColor(R.color.colorPrimaryTransparent)
                                    )
                                    .from(resource)
                                    .into(imgbg)
                            }
                        }
                    })
            }
        }
    }


}