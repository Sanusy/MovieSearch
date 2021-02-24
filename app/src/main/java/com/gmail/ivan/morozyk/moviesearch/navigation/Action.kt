package com.gmail.ivan.morozyk.moviesearch.navigation

sealed class Action {
    data class FromTitleListToTitleDetails(val titleId: String) : Action()
    data class FromPersonListToPersonDetails(val personId: String) : Action()
    data class FromPersonDetailsToTitleDetails(val titleId: String) : Action()
    data class FromTitleDetailsToPersonDetails(val personId: String) : Action()
}
