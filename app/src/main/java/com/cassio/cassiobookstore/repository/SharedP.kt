package com.cassio.cassiobookstore.repository

import android.content.Context
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.model.Books
import com.google.gson.Gson

fun loadFavsFromShared(context: Context?): Books {
    val sharedPref = context?.getSharedPreferences("books_pref", Context.MODE_PRIVATE)
    val dataKey = context?.resources?.getString(R.string.favorites_key)
    val dataRawStr = sharedPref?.getString(dataKey, "")
    val retorno = Gson().fromJson(dataRawStr, Books::class.java)
    if(retorno == null){
        var mBooks = Books()
        mBooks.items = arrayListOf()
        return mBooks
    }else{
        return Gson().fromJson(dataRawStr, Books::class.java)
    }
}

fun saveFavsIntoShared(context : Context, books : Books) : Boolean{
    val sharedPref = context.getSharedPreferences("books_pref", Context.MODE_PRIVATE)
    val editor = sharedPref?.edit()

    editor?.let {
        editor.putString(context.getString(R.string.favorites_key), Gson().toJson(books, Books::class.java))
        editor.commit()
        return true
    } ?: return false

}