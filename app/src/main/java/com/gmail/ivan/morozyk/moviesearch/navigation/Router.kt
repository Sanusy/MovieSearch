package com.gmail.ivan.morozyk.moviesearch.navigation

interface Router {

    fun attachRoutingProvider(routingProvider: RoutingProvider)

    fun detachRoutingProvider()

    fun navigate(navigationAction: Action)
}