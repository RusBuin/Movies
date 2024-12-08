package com.loc.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie

@Database(entities = [Movie::class, AllMovie::class], version = 4)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
}
