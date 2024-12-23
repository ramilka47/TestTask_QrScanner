package ru.ramil.customqrscanner.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import ru.ramil.customqrscanner.ui.theme.CustomQrScannerTheme
import ru.ramil.customqrscanner.ui.view_models.SplashState
import ru.ramil.customqrscanner.ui.view_models.SplashViewModel
import kotlin.system.exitProcess

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(viewModel : SplashViewModel,
                 onEndLoad : @Composable ()->Unit) {
    CustomQrScannerTheme {
        val stateScreen = viewModel.loadingData.collectAsState()
        CustomQrScannerTheme {
            Scaffold {
                when (stateScreen.value) {
                    SplashState.Success -> {
                        onEndLoad()
                    }

                    SplashState.Loading -> {
                        Load()
                    }

                    SplashState.Error -> {
                        Error()
                    }
                }
            }
        }

    }
}

@Composable
internal fun Load(){
    Text(
        text = "Hello Splash Screen!",
        modifier = Modifier
    )
}

@Composable
internal fun Error(){
    exitProcess(1)
}