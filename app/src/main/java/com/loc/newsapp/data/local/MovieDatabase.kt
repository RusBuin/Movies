package com.loc.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loc.newsapp.domain.model.Movie

@Database(entities = [Movie::class],version = 2,)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

}