package no.nrk.radio.nav3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import no.nrk.radio.nav3.navigation.BottomSheetNavigationModel
import no.nrk.radio.nav3.navigation.NavigationModel
import no.nrk.radio.nav3.navigation.SeriesNavigationModel
import org.koin.androidx.compose.koinViewModel

class SeriesViewModel : ViewModel()

@Composable
fun SeriesScreen(
    navigationModel: SeriesNavigationModel,
    onNavigate: (NavigationModel) -> Unit,
    onNavigateUp: () -> Unit
) {
    val viewModel = koinViewModel<SeriesViewModel>()
    println("seriesboi $viewModel")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(top = 24.dp, start = 48.dp)
    ) {
        Text(navigationModel.seriesTitle)

        Spacer(Modifier.size(148.dp))

        Button(
            onClick = {
                onNavigateUp()
            }
        ) {
            Text("Go back")
        }

        Button(
            onClick = {
                onNavigate(BottomSheetNavigationModel("Heeeeyyy"))
            }
        ) {
            Text("Bottom sheet :)")
        }
    }
}
