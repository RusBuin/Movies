package com.loc.newsapp.util

object Constants {

    const val USER_SETTINGS = "user_settings"
    const val APP_ENTRY = "app_entry"
    const val API_KEY = "34a4a42edc6a8dff92e50403c11c3fba"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val MOVIES_DATABASE_NAME ="movie_db"

    enum class Theme(val title: String){
        SYSTEM_DEFAULT("system_default"),
        LIGHT_MODE("light"),
        DARK_MODE("dark")
    }
}