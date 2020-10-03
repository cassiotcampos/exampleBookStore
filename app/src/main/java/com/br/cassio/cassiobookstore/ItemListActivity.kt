package com.br.cassio.cassiobookstore

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.br.cassio.cassiobookstore.adapter.SimpleItemRecyclerViewAdapter
import com.br.cassio.cassiobookstore.dummy.DummyContent


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var fabArrowLeft : FloatingActionButton
    private lateinit var vDivider : View
    private lateinit var rvBooks : RecyclerView
    private lateinit var vContainerBooks : LinearLayout

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
                if(vContainerBooks.visibility == View.GONE && twoPane){
                    vContainerBooks.visibility = View.VISIBLE
                    rotateFab(0f)
                }else if(twoPane){
                    vContainerBooks.visibility = View.GONE
                    rotateFab(180f)
                }
            }
        }

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun rotateFab(rotation: Float) {
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fabArrowLeft).rotation(rotation).withLayer().setDuration(300)
            .setInterpolator(interpolator).start()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }
}