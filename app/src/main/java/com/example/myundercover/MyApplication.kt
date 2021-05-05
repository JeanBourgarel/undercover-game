package com.example.myundercover

import android.app.Application
import com.example.myundercover.fragments.GameViewModel
import com.example.myundercover.fragments.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val myModule = module {
    viewModel { HomeViewModel() }
    viewModel { GameViewModel() }
}

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(myModule)
        }
    }
}