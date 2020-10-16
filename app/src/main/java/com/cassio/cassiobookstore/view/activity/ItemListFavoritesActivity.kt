package com.cassio.cassiobookstore.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.databinding.ActivityItemListBinding
import com.cassio.cassiobookstore.model.dto.BookDTO
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter
import com.cassio.cassiobookstore.view.fragment.ItemDetailFragment
import com.cassio.cassiobookstore.viewmodel.BookListFavoritesViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_list.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class ItemListFavoritesActivity : ItemListActivityBase(), LastBookBindedListener,
    BookClickListener {

    private val vm: BookListFavoritesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        setupToolbar()

        vm.myBooks.observe(this, myBooksObserver)

        setupRecyclerView(binding.root.item_list, BookListDTO())
    }

    private val myBooksObserver = Observer<BookListDTO> {
        if(it.bookDTOS.isNullOrEmpty()){
            onBackPressed()
        }else {
            binding.root.item_list.adapter =
                ListItemAdapter(this as Context, it, this, this)
        }
    }

    override fun onResume() {
        super.onResume()
        vm.loadBooks()
    }

    override fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setBackgroundColor(resources.getColor(R.color.colorRed))
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = resources.getString(R.string.my_favorites)
        supportActionBar?.setIcon(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_action_fav,
                theme
            )
        )
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


    override fun onCreateOptionsMenu(mMenu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions, mMenu)
        mMenu.findItem(R.id.action_favorite).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ItemDetailFragment.REQUEST_VIEW_FAVORITE) {
            if (resultCode == RESULT_OK) {
                val unfavoritedId: String? =
                    data?.getStringExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED)
                unfavoritedId.let {

                    var dummyBook: BookDTO =
                        BookDTO()
                    dummyBook.id = it
                    (rvBooks.adapter as ListItemAdapter).remove(dummyBook)
                }
            }
        }
    }

    override fun loadMoreBooks() {
        // nothing to do here
    }
}

