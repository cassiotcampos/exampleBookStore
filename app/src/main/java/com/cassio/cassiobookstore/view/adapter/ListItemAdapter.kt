package com.cassio.cassiobookstore.view.adapter


import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.databinding.ItemListContentBinding
import com.cassio.cassiobookstore.model.dto.BookDTO
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.view.ImageUtils
import com.cassio.cassiobookstore.view.activity.BookClickListener
import com.cassio.cassiobookstore.view.activity.LastBookBindedListener
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter.BookViewHolder

class ListItemAdapter(
    val context: Context,
    private var mBookListDTO: BookListDTO,
    private val lastBookBindedListener: LastBookBindedListener,
    private val onBookClickListener: BookClickListener
) : RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemListContentBinding =
            ItemListContentBinding.inflate(inflater, parent, false)

        binding.clickListeners = onBookClickListener

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(BookVO(mBookListDTO?.bookDTOS?.get(position)!!))
        if(mBookListDTO?.bookDTOS?.size?.minus(1) == position){
            lastBookBindedListener.loadMoreBooks()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mBookListDTO?.bookDTOS?.get(position) is BookDTO) {
            return position
        } else {
            return -1
        }
    }

    override fun getItemCount(): Int {
        mBookListDTO?.bookDTOS?.size?.let {
            return it
        } ?: return 0
    }


    fun addAll(mBookDTOS: MutableList<BookDTO>) {

        val positionStart = mBookDTOS.size + mBookListDTO!!.bookDTOS.size
        val itensToBeAdded = mBookDTOS.size

        mBookListDTO!!.bookDTOS.addAll(mBookDTOS)
        notifyItemRangeInserted(positionStart, itensToBeAdded)
    }

    fun remove(mBookDTO: BookDTO) {
        var positionToBeRemoved: Int = 0
        for (i in 0 until mBookListDTO!!.bookDTOS.size) {
            if (mBookListDTO!!.bookDTOS[i].id.equals(mBookDTO.id)) {
                positionToBeRemoved = i
            }
        }
        mBookListDTO!!.bookDTOS.removeAt(positionToBeRemoved);
        notifyItemRemoved(positionToBeRemoved)
    }

    inner class BookViewHolder(val viewBinding: ItemListContentBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(bookVO : BookVO) {
            viewBinding.bookVO = bookVO
            var imgThumb: ImageView = viewBinding.root.findViewById(R.id.imgmini)
            var imgBg: ImageView = viewBinding.root.findViewById(R.id.imgbg)

            if (!bookVO.isThumbAvailable()) {
                loadDefaultsImg(imgThumb, imgBg)
            } else {
                ImageUtils.loadImgAndBgFromUrlWithOneRequest(
                    context,
                    bookVO.getThumbUrl(),
                    targetImgViewThumb = imgThumb,
                    targetImgViewBg = imgBg,
                    withTransition = true,
                    priority = Priority.IMMEDIATE
                )
            }

            /*if (position == mBooks.value?.items?.size?.minus(1)) {
                lastBookBindedListener.loadMoreBooks()
            }*/
        }

        private fun loadDefaultsImg(imgThumb : ImageView, imgBg : ImageView) {
            imgBg.setImageDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            )

            imgThumb.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )

            imgThumb.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_image_white))
        }
/*
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