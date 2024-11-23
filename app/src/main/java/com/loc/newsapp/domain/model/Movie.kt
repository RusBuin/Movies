package com.loc.newsapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("poster_path") val poster: String,
    val title: String,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("original_language") val originalLanguage: String
): Parcelable