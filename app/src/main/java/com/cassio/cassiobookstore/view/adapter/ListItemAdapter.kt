package com.cassio.cassiobookstore.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.databinding.ItemListContentBinding
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.view.BookClickListener
import com.cassio.cassiobookstore.view.LastBookBindedListener
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter.BookViewHolder

class ListItemAdapter(
    private val mBooks: MutableLiveData<Books>,
    private val lifecycleOwner: LifecycleOwner,
    val lastBookBindedListener: LastBookBindedListener,
    val onBookClickListener: BookClickListener
) : RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemListContentBinding =
            ItemListContentBinding.inflate(inflater, parent, false)

        binding.lifecycleOwner = this@ListItemAdapter.lifecycleOwner
        binding.clickListeners = onBookClickListener

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        // previne RV de reciclar novas views com informacoes de outras
        if (mBooks.value?.items?.get(position) is Item) {
            return position
        } else {
            return -1
        }
    }

    override fun getItemCount(): Int {
        mBooks.value?.items?.size?.let {
            return it
        } ?: return 0
    }



    fun addAll(mItems: MutableList<Item>) {


        val positionStart = mItems.size + mBooks.value!!.items.size
        val itensToBeAdded = mItems.size

        mBooks.value!!.items.addAll(mItems)
        notifyItemRangeInserted(positionStart, itensToBeAdded)
    }

    fun remove(mItem: Item) {
        var positionToBeRemoved: Int = 0
        for (i in 0 until mBooks.value!!.items.size) {
            if (mBooks.value!!.items[i].id.equals(mItem.id)) {
                positionToBeRemoved = i
            }
        }
        mBooks.value!!.items.removeAt(positionToBeRemoved);
        notifyItemRemoved(positionToBeRemoved)
    }

    inner class BookViewHolder(val viewBinding: ItemListContentBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(position: Int) {

            //viewBinding.setLifecycleOwner(lifecycleOwner);

            val row = mBooks.value!!.items[position]
            viewBinding.bookRow = row

            if (position == mBooks.value?.items?.size?.minus(1)) {
                lastBookBindedListener.loadMoreBooks(row)
            }

            //viewBinding.executePendingBindings();
        }

        /*private fun loadDefaultsImg() {
            this@BookViewHolder.imgbg?.setImageDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        parentActivity.baseContext,
                        R.color.colorPrimary
                    )
                )
            )

            this@BookViewHolder.imgmini?.setBackgroundColor(
                ContextCompat.getColor(
                    parentActivity.baseContext,
                    R.color.colorPrimary
                )
            )
        }

        fun bind(item: Item) {
            this@BookViewHolder.imgmini?.setBackgroundColor(
                ContextCompat.getColor(
                    parentActivity.baseContext,
                    android.R.color.transparent
                )
            )
            this@BookViewHolder.title?.text = item.volumeInfo.title
            this@BookViewHolder.containerButton?.setOnClickListener(onClickItem(item))
            updateWithUrl(item)
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
                    parentActivity.startActivityForResult(
                        intent,
                        ItemDetailFragment.REQUEST_VIEW_FAVORITE
                    )
                }
            }
        }

        private fun updateWithUrl(item: Item) {
            item.volumeInfo.imageLinks?.smallThumbnail?.let {
                loadFromUrl(it)
            } ?: loadDefaultsImg()
        }

        private fun loadFromUrl(url: String) {

        }*/
    }
}