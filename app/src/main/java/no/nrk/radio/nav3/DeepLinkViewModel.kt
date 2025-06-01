package no.nrk.radio.nav3

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.nrk.radio.nav3.navigation.FrontPageNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.PlayerNavigationModel
import no.nrk.radio.nav3.navigation.SeriesNavigationModel

class DeepLinkViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _navigations = MutableStateFlow<List<NavigationModel>?>(null)
    val navigations = _navigations.asStateFlow()

    private var handled by savedStateHandle.saved { false }

    fun parseDeepLink(data: Uri?) {
        if (handled) {
            return
        }

        println("parsing deeplink $data")

        viewModelScope.launch(
            CoroutineExceptionHandler { _, _ ->
                _navigations.value = listOf(FrontPageNavigationModel)
            }
        ) {
            delay(2500)
            _navigations.value = data.parseDeeplink()
        }
    }

    fun setHandled() {
        handled = true
        _navigations.value = null
    }
}

private fun Uri?.parseDeeplink(): List<NavigationModel> {
    return when (this?.path) {
        "/series" -> listOf(
            FrontPageNavigationModel, SeriesNavigationModel("from deeplink")
        )

        "/player" -> listOf(
            FrontPageNavigationModel, SeriesNavigationModel("from deeplink, after player"), PlayerNavigationModel("from deeplinky")
        )

        else -> listOf(
            FrontPageNavigationModel
        )
    }
}
