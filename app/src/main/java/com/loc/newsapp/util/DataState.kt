package com.loc.newsapp.util


sealed class UIComponent {

    data class Toast(val message: String): UIComponent()


}