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
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Books
import com.cassio.cassiobookstore.model.Item
import com.cassio.cassiobookstore.repository.BooksApi
import com.cassio.cassiobookstore.repository.loadFavsFromShared
import com.cassio.cassiobookstore.view.adapter.ListItemAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemListActivity : AppCompatActivity(), ListItemAdapter.LastItemLoadedListener {

    private final val extraFavKey: String = "EXTRA_FAV"

    private lateinit var booksResult: Books
    private val maxResults = 40

    private var twoPane: Boolean = false
    private lateinit var fabArrowLeft: FloatingActionButton
    private lateinit var vDivider: View
    private lateinit var rvBooks: RecyclerView
    private lateinit var vContainerBooks: LinearLayout

    private var apiIndex: Int = 0

    val isFav: Boolean
        get() {
            return intent.getBooleanExtra(extraFavKey, false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (isFav) {
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

        rvBooks = findViewById(R.id.item_list)


        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            fabArrowLeft = findViewById(R.id.fab)
            vContainerBooks = findViewById(R.id.item_list_container)
            fabArrowLeft = findViewById(R.id.fab);
            vDivider = findViewById(R.id.my_divider)
            twoPane = true

            fabArrowLeft.setOnClickListener {
                if (vContainerBooks.visibility == View.GONE && twoPane) {
                    vContainerBooks.visibility = View.VISIBLE
                    rotateFab(0f)
                } else if (twoPane) {
                    vContainerBooks.visibility = View.GONE
                    rotateFab(180f)
                }
            }
        }

        if (!isFav) loadFromApi() else loadFromDisk()
    }

    private fun loadFromDisk() {
        setupRecyclerView(loadFavsFromShared(this))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions, menu)
        if (!isFav) menu.findItem(R.id.action_favorite).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == R.id.action_favorite) {
            if (loadFavsFromShared(this).items.size > 0) {
                val intent = Intent(baseContext, ItemListActivity::class.java)
                intent.putExtra(extraFavKey, true)
                startActivity(intent)
            }else {
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

    private fun setupRecyclerView(tempResult: Books) {
        booksResult = tempResult

        rvBooks.apply {
            rvBooks.layoutManager = GridLayoutManager(this@ItemListActivity, 2)
            rvBooks.adapter =
                ListItemAdapter(this@ItemListActivity, booksResult, twoPane, this@ItemListActivity)
        }
    }

    private fun loadFromApi() {

        BooksApi.getInstance()
            .getBooks(
                "android",
                maxResults,
                apiIndex,
                (object : Callback<Books> {
                    override fun onResponse(call: Call<Books>?, response: Response<Books>?) {

                        if (response != null) {
                            val tempResult: Books = response.body()!!
                            if (tempResult.items != null) {
                                if (apiIndex == 0) {
                                    setupRecyclerView(tempResult)
                                } else {
                                    val mAdapter = rvBooks.adapter as ListItemAdapter
                                    mAdapter.addAll(ArrayList(tempResult.items))
                                }
                                apiIndex += maxResults
                            }
                        }
                    }

                    override fun onFailure(call: Call<Books>?, t: Throwable?) {
                        Snackbar.make(
                            rvBooks, "Api error",
                            Snackbar.LENGTH_LONG
                        ).setAction("Try Again") {
                            loadFromApi()
                        }.show();
                    }
                })
            )
    }

    override fun onLastItemLoaded() {
        if (!isFav)
            loadFromApi()
    }

    fun favRemoved(item: Item) {
        if (isFav)
            (rvBooks.adapter as ListItemAdapter).remove(item)
    }

    fun favReAdded(item: Item) {
        if (isFav)
            (rvBooks.adapter as ListItemAdapter).addAll(arrayListOf(item))
    }
}

