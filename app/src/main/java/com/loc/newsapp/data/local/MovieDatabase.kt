package com.loc.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loc.newsapp.domain.model.Movie

@Database(entities = [Movie::class],version = 1,)
//@TypeConverters(NewsTypeConvertor::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

}