package com.demo.tvshow

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.tvshow.data.dao.TVShowDao
import com.demo.tvshow.data.entity.TVShowEntity

@Database(
    entities = [TVShowEntity::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tvShowDao(): TVShowDao

}