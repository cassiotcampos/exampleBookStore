package com.br.cassio.cassiobookstore.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.cassio.cassiobookstore.ItemDetailActivity
import com.br.cassio.cassiobookstore.ItemDetailFragment
import com.br.cassio.cassiobookstore.ItemListActivity
import com.br.cassio.cassiobookstore.R
import com.br.cassio.cassiobookstore.model.generated.java.Books
import com.br.cassio.cassiobookstore.model.generated.java.Item
import com.google.gson.Gson

class SimpleItemRecyclerViewAdapter(
    private val parentActivity: ItemListActivity,
    private val values: Books,
    private val twoPane: Boolean
) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Item
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
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_BOOK, Gson().toJson(item))
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.items[position]
        holder.idView.text = item.volumeInfo.title
        // holder.contentView.text = item.volumeInfo.description

        with(holder.itemView) {
            tag = item as Item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.id_text)
        val contentView: TextView = view.findViewById(R.id.content)
    }
}