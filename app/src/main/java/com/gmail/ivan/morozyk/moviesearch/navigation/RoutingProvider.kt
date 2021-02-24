package com.gmail.ivan.morozyk.moviesearch.navigation

fun interface RoutingProvider {

    fun navigate(navigationAction: Action)
}