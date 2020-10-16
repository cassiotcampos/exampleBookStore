package com.cassio.cassiobookstore.model.vo

import android.view.View

abstract class BaseVO {

    protected fun getVisibility(isAvailable : Boolean) : Int{
        if(isAvailable) return View.VISIBLE
        return View.GONE
    }
}
