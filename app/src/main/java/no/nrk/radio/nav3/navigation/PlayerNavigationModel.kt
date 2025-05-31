package no.nrk.radio.nav3.navigation

import kotlinx.serialization.Serializable

@Serializable
data class PlayerNavigationModel(
    val title: String
) : NavigationModel, SingleNavigationEntry
