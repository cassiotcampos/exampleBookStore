package com.cassio.cassiobookstore.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.databinding.ActivityItemListBinding
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter
import com.cassio.cassiobookstore.view.fragment.ItemDetailFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

interface LastBookBindedListener {
    fun loadMoreBooks()
}

interface BookClickListener {
    fun onBookClicked(bookVO: BookVO)
}

abstract class ItemListActivityBase : AppCompatActivity(), LastBookBindedListener, BookClickListener {

    lateinit var binding: ActivityItemListBinding

    lateinit var fabArrowLeft: FloatingActionButton
    private lateinit var vDivider: View
    lateinit var rvBooks: RecyclerView
    private lateinit var vContainerBooks: LinearLayout

    internal abstract fun setupToolbar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar()

        if (binding.root.findViewById<NestedScrollView>(R.id.item_detail_container_two_panel) != null) {
            fabArrowLeft = findViewById(R.id.fab)
            vContainerBooks = findViewById(R.id.item_list_container)
            fabArrowLeft = findViewById(R.id.fab);
            vDivider = findViewById(R.id.my_divider)

            fabArrowLeft.setOnClickListener {
                if (vContainerBooks.visibility == View.GONE) {
                    vContainerBooks.visibility = View.VISIBLE
                    rotateFab(0f)
                } else {
                    vContainerBooks.visibility = View.GONE
                    rotateFab(180f)
                }
            }
        }
    }

    protected fun setupRecyclerView(mRecyclerView: RecyclerView, mData : BookListDTO) {
        if (mRecyclerView.adapter == null) {
            val mAdapter: ListItemAdapter =
                ListItemAdapter(this as Context, mData, this, this)

            mRecyclerView.layoutManager = GridLayoutManager(this, 2)
            mRecyclerView.adapter = mAdapter
        }
    }

    internal fun isTwoPane(): Boolean {
        return this@ItemListActivityBase.findViewById<View>(R.id.item_detail_container_two_panel) != null
    }

    override fun onBookClicked(bookVO: BookVO) {
        if (isTwoPane()) {

            // start fragment
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(bookVO))
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container_two_panel, fragment)
                .commit()

        } else {

            // start activity
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(bookVO))
            }
            startActivityForResult(intent, ItemDetailFragment.REQUEST_VIEW_FAVORITE)
        }
    }

    private fun rotateFab(rotation: Float) {
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fabArrowLeft).rotation(rotation).setDuration(500)
            .setInterpolator(interpolator).setStartDelay(500).start()
    }
}

