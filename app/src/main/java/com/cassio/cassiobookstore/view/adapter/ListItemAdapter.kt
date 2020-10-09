package com.cassio.cassiobookstore.view.adapter


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.view.ItemDetailActivity
import com.cassio.cassiobookstore.view.ItemDetailFragment
import com.cassio.cassiobookstore.view.ItemListActivity
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter.BookViewHolder
import com.google.gson.Gson

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
        holder.bind(item)

        if (position == values.items.size - 1) {
            onLastItemLoaded.onLastItemLoaded()
        }
    }

    override fun getItemViewType(position: Int): Int {
        // previne RV de reciclar novas views com informacoes de outras
        if(values.items[position] is Item){
            return position
        }else{
            return -1
        }
    }

    override fun getItemCount() = values.items.size

    fun addAll(mItems: MutableList<Item>) {
        for (i in 0..mItems.size - 1) {
            if (values.items.add(mItems[i])) {
                notifyItemInserted(values.items.size)
            }
        }
    }

    fun remove(mItem: Item) {
        var positionToBeRemoved: Int = 0
        for (i in 0..values.items.size - 1) {
            if (values.items[i].id.equals(mItem.id)) {
                positionToBeRemoved = i
            }
        }
        values.items.removeAt(positionToBeRemoved);
        notifyItemRemoved(positionToBeRemoved)
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView? = null
        var imgmini : ImageView? = null
        var imgbg : ImageView? = null
        var containerButton : LinearLayout? = null

        init {
            title = view.findViewById(R.id.books_title)
            imgmini = view.findViewById(R.id.imgmini)
            imgbg = view.findViewById(R.id.imgbg)
            containerButton = view.findViewById(R.id.container_book_button)
        }

        private fun loadDefaultsImg() {
            this@BookViewHolder.imgbg?.setImageDrawable(ColorDrawable(ContextCompat.getColor(parentActivity.baseContext, R.color.colorPrimary)))
            this@BookViewHolder.imgmini?.setImageDrawable(ContextCompat.getDrawable(parentActivity.baseContext, R.drawable.ic_livro))
        }

        fun bind(item : Item) {
            updateWithUrl(item)
            this@BookViewHolder.title?.text = item.volumeInfo.title
            this@BookViewHolder.containerButton?.setOnClickListener (onClickItem(item))
        }

        private fun onClickItem(item: Item): View.OnClickListener? {

            return View.OnClickListener {
                if (twoPane) {

                    // start fragment
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

                    // start activity
                    val intent = Intent(parentActivity, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_BOOK, Gson().toJson(item))
                    }
                    parentActivity.startActivity(intent)
                }
            }
        }

        @UiThread
        fun updateWithUrl(item : Item) {
            item.volumeInfo.imageLinks?.smallThumbnail?.let {
                loadFromUrl(it)
            } ?: loadDefaultsImg()
        }

        private fun loadFromUrl(url: String) {
            val myOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(parentActivity)
                .asBitmap()
                .load(url.replace("http://", "https://"))
                .apply(myOptions)
                .into(object : SimpleTarget<Bitmap?>() {

                    @UiThread
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        this@BookViewHolder.imgmini?.setImageBitmap(resource)
                        this@BookViewHolder.imgbg?.setImageBitmap(Bitmap.createScaledBitmap(resource, 3, 3, true))


                    }
                })
        }
    }


}