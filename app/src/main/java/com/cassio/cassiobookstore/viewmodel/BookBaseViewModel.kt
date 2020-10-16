package com.cassio.cassiobookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.cassio.cassiobookstore.repository.SharedP


abstract class BookBaseViewModel(
    application: Application,
    private val sharedP: SharedP,
) :
    AndroidViewModel(application), LifecycleObserver {

}