package com.cassio.cassiobookstore.adapter


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.ItemDetailActivity
import com.cassio.cassiobookstore.ItemDetailFragment
import com.cassio.cassiobookstore.ItemListActivity
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.generated.java.Books
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

class ListItemAdapter(
    private val parentActivity: ItemListActivity,
    private var values: Books,
    private val twoPane: Boolean,
    private val onLastItemLoaded: LastItemLoadedListener,
) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {


    interface LastItemLoadedListener {
        fun onLastItemLoaded()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parentActivity)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemAdapter.ViewHolder, position: Int) {


        val item = values.items[position]

        holder.setTitle(item.volumeInfo.title)
        item.volumeInfo?.imageLinks?.smallThumbnail?.let { holder.updateWithUrl(it) }
            ?: holder.placeHolderBackground()

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

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.books_title)
        private val imgmini: ImageView = view.findViewById(R.id.imgmini)
        private val imgbg: ImageView = view.findViewById(R.id.imgbg)
        val containerButton: LinearLayout = view.findViewById(R.id.container_book_button)


        fun updateWithUrl(url: String) {
            if (url.isNotBlank() && url.isNotEmpty()) {

                Picasso.get()
                    .load(Uri.parse(url.replace("http://", "https://")))
                    .error(R.drawable.ic_book_placeholder)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .into(imgmini)

                val transformationBlur = BlurTransformation(parentActivity, 25, 2)
                val transformationAlphaLight = AlphaTransformation(0.35f)

                Picasso.get()
                    .load(Uri.parse(url.replace("http://", "https://")))
                    .transform(transformationBlur)
                    .transform(transformationAlphaLight)
                    .into(imgbg)
            } else {
                imgmini.setImageDrawable(
                    ContextCompat.getDrawable(
                        parentActivity, // Context
                        R.drawable.ic_book_placeholder // Drawable
                    )
                )

                imgbg.setImageDrawable(
                    ContextCompat.getDrawable(
                        parentActivity, //  Context
                        R.drawable.ic_book_placeholder // Drawable
                    )
                )
            }
        }

        fun setTitle(titleStr: String?) {
            title.text = titleStr
        }

        fun placeHolderBackground() {
            imgbg.setImageDrawable(
                ContextCompat.getDrawable(
                    parentActivity, //  Context
                    R.drawable.ic_book_placeholder // Drawable
                )
            )
        }
    }


}