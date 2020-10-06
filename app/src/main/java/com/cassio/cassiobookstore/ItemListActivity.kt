package com.cassio.cassiobookstore

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cassio.cassiobookstore.adapter.ListItemAdapter
import com.cassio.cassiobookstore.model.generated.java.Books
import com.cassio.cassiobookstore.repository.retrofit.bookapi.BooksApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemListActivity : AppCompatActivity(), ListItemAdapter.LastItemLoadedListener {

    private lateinit var booksResult: Books
    private val maxResults = 10

    private var twoPane: Boolean = false
    private lateinit var fabArrowLeft: FloatingActionButton
    private lateinit var vDivider: View
    private lateinit var rvBooks: RecyclerView
    private lateinit var vContainerBooks: LinearLayout

    private var apiIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        rvBooks = findViewById(R.id.item_list)


        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            fabArrowLeft = findViewById(R.id.fab)
            vContainerBooks = findViewById(R.id.item_list_container)
            fabArrowLeft = findViewById<FloatingActionButton>(R.id.fab);
            vDivider = findViewById<View>(R.id.my_divider)
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

        loadFromApi()
    }

    private fun rotateFab(rotation: Float) {
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fabArrowLeft).rotation(rotation).withLayer().setDuration(300)
            .setInterpolator(interpolator).start()
    }

    private fun setupRecyclerView(tempResult: Books) {
        booksResult = tempResult

        rvBooks.apply {
            this.setHasFixedSize(true)
            rvBooks.layoutManager = GridLayoutManager(this@ItemListActivity, 2)
            adapter = ListItemAdapter(this@ItemListActivity, booksResult, twoPane, this@ItemListActivity)
        }
    }

    private fun loadFromApi() {

        BooksApi.getInstance()
            .getBooks(
                "android",
                maxResults,
                apiIndex,
                (object : Callback<Books> {
                    override fun onResponse(
                        call: Call<Books>?,
                        response: Response<Books>?
                    ) {

                        if (response != null) {

                            val tempResult: Books = response.body()!!
                            if (tempResult.items != null) {

                                if (apiIndex == 0) {
                                    setupRecyclerView(tempResult)
                                } else {
                                    booksResult.totalItems = tempResult.totalItems
                                    booksResult.items.addAll(tempResult.items)
                                    rvBooks.adapter?.notifyDataSetChanged()
                                }
                                apiIndex += maxResults
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<Books>?,
                        t: Throwable?
                    ) {
                        throw t!!
                    }
                })
            )
    }

    override fun onLastItemLoaded() {
        loadFromApi()
    }
}