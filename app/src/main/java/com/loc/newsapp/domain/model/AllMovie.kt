package com.loc.newsapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "all_movies")
data class AllMovie(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("poster_path") val poster: String?,
    val title: String,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("original_language") val originalLanguage: String,
    val popularity: Double
): Parcelable



 fun AllMovie.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        poster = this.poster,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        originalLanguage = this.originalLanguage,
        overview = this.overview,
        popularity = this.popularity)
}

