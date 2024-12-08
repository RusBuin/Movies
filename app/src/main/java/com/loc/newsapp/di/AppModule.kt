package com.loc.newsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.data.local.MovieDatabase
import com.loc.newsapp.data.manger.LocalUserMangerImpl
import com.loc.newsapp.data.remote.MovieApi
import com.loc.newsapp.data.remote.NetworkHelper
import com.loc.newsapp.data.repository.MovieRepositoryImpl
import com.loc.newsapp.domain.manger.LocalUserManger
import com.loc.newsapp.domain.repository.MovieRepository

import com.loc.newsapp.domain.usecases.movies.GetMovie
import com.loc.newsapp.util.Constants.BASE_URL
import com.loc.newsapp.util.Constants.MOVIES_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)


    @Provides
    @Singleton
    fun provideApiInstance(): MovieApi {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        movieApi: MovieApi,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(movieApi,movieDao)
    }



    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ): MovieDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = MovieDatabase::class.java,
            name = MOVIES_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
            return NetworkHelper(context)
        }
    }


    @Provides
    @Singleton
    fun provideNewsDao(
        movieDatabase: MovieDatabase
    ): MovieDao = movieDatabase.movieDao

}