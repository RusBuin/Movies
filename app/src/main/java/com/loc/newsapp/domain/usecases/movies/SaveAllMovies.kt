package com.loc.newsapp.domain.usecases.movies

import android.util.Log
import com.loc.newsapp.data.remote.MovieApi
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.repository.MovieRepository
import javax.inject.Inject

class SaveAllMovies @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieApi: MovieApi
) {
    private val TAG = "SaveAllMovies"

    suspend operator fun invoke() {
        var page = 1
        var allMovies = mutableListOf<AllMovie>()
        var totalPages: Int

        Log.d(TAG, "Загрузка фильмов началась")

        // Загружаем фильмы постранично
        do {
            try {
                // Получаем страницу с фильмами
                Log.d(TAG, "Загрузка страницы $page")
                val response = movieApi.getMovies(page)

                val movies = response.results.map { movieResult ->
                    AllMovie(
                        id = movieResult.id,
                        title = movieResult.title,
                        overview = movieResult.overview,
                        poster = movieResult.poster,
                        releaseDate = movieResult.releaseDate,
                        voteAverage = movieResult.voteAverage,
                        originalLanguage = movieResult.originalLanguage
                    )
                }

                // Добавляем фильмы в список
                allMovies.addAll(movies)

                movies.forEach {
                    Log.d(TAG, "Загружен фильм: ${it.title}")
                }

                totalPages = response.totalPages
                Log.d(TAG, "Получено $totalPages страниц. Загружено фильмов на странице $page.")
                page++ // Переходим к следующей странице

            } catch (e: Exception) {
                // Логируем ошибку, если запрос не удается
                Log.e(TAG, "Ошибка при загрузке страницы $page", e)
                break
            }
        } while (page <= totalPages) // Прекращаем, когда все страницы загружены

        // Сохраняем все фильмы в локальную базу данных
        if (allMovies.isNotEmpty()) {
            Log.d(TAG, "Сохранение ${allMovies.size} фильмов в базу данных...")
            movieRepository.insertAllMovies(allMovies)
            Log.d(TAG, "Все фильмы успешно сохранены в базу данных.")
        } else {
            Log.d(TAG, "Нет фильмов для сохранения.")
        }
    }
}
