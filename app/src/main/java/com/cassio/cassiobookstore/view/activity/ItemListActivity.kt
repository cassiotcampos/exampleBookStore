package com.cassio.cassiobookstore.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.databinding.ActivityItemListBinding
import com.cassio.cassiobookstore.model.dto.BookListDTO
import com.cassio.cassiobookstore.model.vo.BookVO
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter
import com.cassio.cassiobookstore.view.fragment.ItemDetailFragment
import com.cassio.cassiobookstore.viewmodel.BookListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_list_and_detail.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class ItemListActivity : ItemListActivityBase(), LastBookBindedListener, BookClickListener {

    private var hasFavorites: Boolean = false
    private val vm: BookListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = vm

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            vm.hasFavorites.observe(this, hasFavoriteObserver)
            vm.showError.observe(this, showErrorObserver)
            vm.lastLoadedBooks.observe(this, myBooksObserver)
            val emptyData: BookListDTO = BookListDTO()
            emptyData.bookDTOS = arrayListOf()
            setupRecyclerView(binding.root.item_list, emptyData)
        } else {
            setupRecyclerView(binding.root.item_list, vm.myBooks.value!!)
        }

    }

    private val myBooksObserver = Observer<BookListDTO> {
        addToList(it)
    }

    private fun addToList(it: BookListDTO) {
        if (it.bookDTOS.isNullOrEmpty().not()) {
            if (binding.root.item_list.adapter is ListItemAdapter) {
                (binding.root.item_list.adapter as ListItemAdapter).addAll(it.bookDTOS)
            }
        }
    }

    override fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        supportActionBar?.setHomeButtonEnabled(false);
        supportActionBar?.setDisplayHomeAsUpEnabled(false);
        supportActionBar?.title = resources.getString(R.string.app_name)
        supportActionBar?.setIcon(null)
    }

    override fun onResume() {
        super.onResume()
        vm.verifyIfHasFavorites()
    }

    private val hasFavoriteObserver = Observer<Boolean> {
        hasFavorites = it
        invalidateOptionsMenu()
    }

    private val showErrorObserver = Observer<String> {
        Snackbar.make(
            binding.root.findViewById(R.id.item_list)!!, "Api error",
            Snackbar.LENGTH_LONG
        ).setAction("Try Again") {
            vm.loadBooks()
        }.show();
    }

    // called inside adapter when user scrolls to the end of the list
    override fun loadMoreBooks() {
        vm.loadBooks()
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
        mMenu.findItem(R.id.action_favorite).isVisible = hasFavorites
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_favorite) {
            if (hasFavorites) {
                val intent = Intent(baseContext, ItemListFavoritesActivity::class.java)
                startActivity(intent)
            }
            return true
        } else if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun rotateFab(rotation: Float) {
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fabArrowLeft).rotation(rotation).setDuration(500)
            .setInterpolator(interpolator).setStartDelay(500).start()
    }


    /*fun favRemoved(item: Item) {
        if (isFav)
            (rvBooks.adapter as ListItemAdapter).remove(item)
    }

    fun favReAdded(item: Item) {
        if (isFav)
            (rvBooks.adapter as ListItemAdapter).addAll(arrayListOf(item))
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isShowingFavorites) {
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
    }*/
}

