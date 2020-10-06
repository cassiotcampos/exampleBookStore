package com.cassio.cassiobookstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cassio.cassiobookstore.model.generated.java.Item
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson

class ItemDetailFragment : Fragment() {

    private var bookStr : String? = null
    lateinit var bookMaster : Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_BOOK)) {
                bookStr = it.getString(ARG_BOOK)
                bookMaster = Gson().fromJson(bookStr, Item::class.java)
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = bookMaster.volumeInfo.title
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        bookMaster.let {
            //rootView.findViewById<TextView>(R.id.item_detail).text = it.volumeInfo.description
        }

        return rootView
    }

    companion object {
        const val ARG_BOOK = "book"
    }

}