package ru.ramil.customqrscanner.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ramil.customqrscanner.ui.theme.CustomQrScannerTheme
import ru.ramil.customqrscanner.ui.utils.withPermissionCamera
import ru.ramil.customqrscanner.ui.view_models.MainViewModel
import ru.ramil.customqrscanner.ui.view_models.RequestPermission

@Composable
fun MainScreen(
    viewModel : MainViewModel,
    toCustomScanner : ()->Unit
) {
    val state = viewModel.state.collectAsState()

    when(state.value){
        RequestPermission.Idle -> { }
        RequestPermission.RequestPermissionForCustom -> {
            withPermissionCamera {
                toCustomScanner()
            }
        }
    }

    MainScreenContent(
        toCustomScanner = viewModel::toCustomScanner
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MainScreenContent(
    toCustomScanner : ()->Unit
){
    CustomQrScannerTheme {
        Scaffold {
            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = toCustomScanner) {
                    Text(text = "Открыть qr сканнер")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreenContent(){
    MainScreenContent(
        toCustomScanner = {})
}