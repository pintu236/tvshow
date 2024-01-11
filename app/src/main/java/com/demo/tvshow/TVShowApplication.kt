package com.demo.tvshow

import android.app.Application
import android.content.Context
import androidx.room.Room

class TVShowApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "tv_shows_database"
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate() {
        super.onCreate()
        mApplicationContext = applicationContext;
    }

    companion object {
        @JvmField
        var mApplicationContext: Context? = null
    }
}