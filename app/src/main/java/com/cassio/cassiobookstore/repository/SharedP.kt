package com.cassio.cassiobookstore.repository

import android.content.Context
import android.content.SharedPreferences
import com.cassio.cassiobookstore.model.dto.BookDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SharedP(private val mContext: Context){

    companion object{
        const val prefKey = "MY_PREF"
        const val favKey = "FAVS"
    }

    private val sharedPref: SharedPreferences = mContext.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
    private var myFavorites : ArrayList<BookDTO> = loadFavsFromShared()

    fun loadFavsFromShared(): ArrayList<BookDTO> {
        val dataRawStr = sharedPref.getString(favKey, null)
        if(dataRawStr.isNullOrEmpty()){
            myFavorites = arrayListOf()
        }else{
            val listType: Type = object : TypeToken<List<BookDTO>>() {}.type
            myFavorites = Gson().fromJson(dataRawStr, listType)
        }

        return myFavorites
    }

    fun addOrRemoveFav(add: Boolean, value: BookDTO) {
        if(add){
            addToFavorites(value)
        }else{
            removeFavorite(value)
        }
    }

    private fun saveFavsIntoShared(bookListDTO: ArrayList<BookDTO>): Boolean {

        val sharedPref = mContext.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        editor?.let {
            editor.putString(favKey, Gson().toJson(bookListDTO))
            editor.apply()
            return true
        } ?: return false
    }

    private fun addToFavorites(value: BookDTO) {
        var itemToAdd : BookDTO? = null
        myFavorites.forEach meuLoop@{
            if (value.getId().equals(it.getId())) {
                itemToAdd = it
                return@meuLoop
            }
        }

        // check if already is on List. If not, add it
        if(itemToAdd == null){
            myFavorites.add(value)
            saveFavsIntoShared(myFavorites)
        }
    }

    private fun removeFavorite(value: BookDTO): Boolean {
        var itemToRemove : BookDTO? = null
        myFavorites.forEach meuLoop@{
            if (value.getId().equals(it.getId())) {
                itemToRemove = it
                return@meuLoop
            }
        }

        if(itemToRemove != null){
            myFavorites.remove(itemToRemove)
            saveFavsIntoShared(myFavorites)
        }
        return false
    }

    fun isFav(bookVO: BookDTO): Boolean {
        myFavorites.forEach meuLoop@{
            if (bookVO.id == it.id) {
                return true
            }
        }
        return false
    }

    fun getFavorites(): ArrayList<BookDTO> {
        return myFavorites
    }

    fun hasFavorites(): Boolean {
        return myFavorites.isNullOrEmpty().not()
    }
}