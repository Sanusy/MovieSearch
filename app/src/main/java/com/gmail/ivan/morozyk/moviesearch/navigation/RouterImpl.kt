package com.gmail.ivan.morozyk.moviesearch.navigation

class RouterImpl(private var routingProvider: RoutingProvider? = null) : Router {

    override fun attachRoutingProvider(routingProvider: RoutingProvider) {
        this.routingProvider = routingProvider
    }

    override fun detachRoutingProvider() {
        routingProvider = null
    }

    override fun navigate(navigationAction: Action) {
        routingProvider?.navigate(navigationAction)
    }
}