package no.nrk.radio.nav3.navigation

import kotlinx.serialization.Serializable

@Serializable
data class SeriesNavigationModel(
    val seriesTitle: String
) : NavigationModel
