package com.cassio.cassiobookstore.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.databinding.ActivityItemListBinding
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter
import com.cassio.cassiobookstore.viewmodel.BookListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_list.view.*
import org.koin.android.viewmodel.ext.android.viewModel

interface LastBookBindedListener {
    fun loadMoreBooks(item: Item?)
}

interface BookClickListener {
    fun onBookClicked(item: Item)
}

class ItemListActivity : AppCompatActivity(), LastBookBindedListener, BookClickListener {

    companion object {
        const val ARG_IS_FAVORITES = "ARG_IS_FAVORITE"
    }

    private val vm: BookListViewModel by viewModel()

    //-------------------- OLD IMPLEMENTATION
    private lateinit var fabArrowLeft: FloatingActionButton
    private lateinit var vDivider: View
    private lateinit var rvBooks: RecyclerView
    private lateinit var vContainerBooks: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityItemListBinding.inflate(layoutInflater)
        binding.viewModel = vm
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (vm.isFav) {
            toolbar.setBackgroundColor(resources.getColor(R.color.colorRed))
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

        // init adapter
        val mRecyclerView : RecyclerView = binding.root.item_list

        if (mRecyclerView.adapter == null) {

            // init adapter
            var emptyBooks = Books()
            emptyBooks.items = arrayListOf()
            val mAdapter : ListItemAdapter =
                ListItemAdapter(MutableLiveData(emptyBooks), this, this, this)

            mRecyclerView.layoutManager = GridLayoutManager(this, 2)
            mRecyclerView.adapter = mAdapter

            if(vm.allBooksLoaded?.items.isNullOrEmpty()) {
                vm.loadBooks()
            } else vm.allBooksLoaded?.items?.let { mAdapter.addAll(it) }
        }

        vm.lastBooksLoaded.observe(this, Observer {
            // binding.root is the only way to get views from included/merged layouts
            if (binding.root.item_list.adapter is ListItemAdapter) {
                (binding.root.item_list.adapter as ListItemAdapter).addAll(it.items)
            }
        })

        vm.showError.observe(this, {
            Snackbar.make(
                rvBooks, "Api error",
                Snackbar.LENGTH_LONG
            ).setAction("Try Again") {
                vm.loadBooks()
            }.show();
        })


        /*rvBooks = findViewById(R.id.item_list)


        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            fabArrowLeft = findViewById(R.id.fab)
            vContainerBooks = findViewById(R.id.item_list_container)
            fabArrowLeft = findViewById(R.id.fab);
            vDivider = findViewById(R.id.my_divider)
            twoPane = true

            fabArrowLeft.setOnClickListener {
                if (vContainerBooks.visibility == View.GONE && twoPane) {
                    vContainerBooks.visibility = View.VISIBLE
                    //rotateFab(0f)
                } else if (twoPane) {
                    vContainerBooks.visibility = View.GONE
                    //rotateFab(180f)
                }
            }
        }*/


    }

    private fun isTwoPane(): Boolean {
        return this@ItemListActivity.findViewById<View>(R.id.item_detail_container_two_panel) != null
    }

    // called inside adapter when user scrolls to the end of the list
    override fun loadMoreBooks(item: Item?) {
        vm.loadBooks()
    }

    override fun onBookClicked(item: Item) {
        if (isTwoPane()) {

            // start fragment
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(item))
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container_two_panel, fragment)
                .commit()

        } else {

            // start activity
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_BOOK_DETAIL, Gson().toJson(item))
            }
            startActivityForResult(intent, ItemDetailFragment.REQUEST_VIEW_FAVORITE)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions, menu)
        if (!vm.isFav) menu.findItem(R.id.action_favorite).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.getItemId()

        if (id == R.id.action_favorite) {

            if (vm.loadFavorites().items.size > 0) {
                val intent = Intent(baseContext, ItemListActivity::class.java)
                intent.putExtra(ItemListActivity.ARG_IS_FAVORITES, true)
                startActivity(intent)

            } else {

                Snackbar.make(
                    rvBooks,
                    getString(R.string.msg_no_favorites),
                    Snackbar.LENGTH_LONG
                ).setAction("OK") {
                }.show();

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (vm.isFav) {
            if (requestCode == ItemDetailFragment.REQUEST_VIEW_FAVORITE) {
                if (resultCode == RESULT_OK) {
                    val unfavoritedId: String? =
                        data?.getStringExtra(ItemDetailFragment.RESULT_ITEM_ID_UNFAVORITED)
                    unfavoritedId.let {

                        var dummyBook: Item = Item()
                        dummyBook.id = it
                        (rvBooks.adapter as ListItemAdapter).remove(dummyBook)
                    }
                }
            }
        }
    }
}

