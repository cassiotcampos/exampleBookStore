package com.cassio.cassiobookstore.di


import android.content.Context
import com.cassio.cassiobookstore.repository.BooksApi
import com.cassio.cassiobookstore.repository.SharedP
import com.cassio.cassiobookstore.viewmodel.BookDetailViewModel
import com.cassio.cassiobookstore.viewmodel.BookListFavoritesViewModel
import com.cassio.cassiobookstore.viewmodel.BookListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val booksApiModule = module {
    single { provideSharedPref(androidContext()) }
    single { provideBooksApi() }


    /*viewModel { (handle : SavedStateHandle?) ->
        BookListViewModel(androidApplication(), get(), get(), handle!!)
    }*/

    viewModel {
        BookListViewModel(androidApplication(), get(), get())
    }

    viewModel {
        BookListFavoritesViewModel(androidApplication(), get())
    }

    viewModel {
        BookDetailViewModel(androidApplication(), get())
    }
}

fun provideBooksApi(): BooksApi? {
    return BooksApi.getInstance()
}

fun provideSharedPref(context: Context): SharedP {
    return SharedP(context)
}