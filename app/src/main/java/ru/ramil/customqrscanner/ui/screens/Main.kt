package ru.ramil.customqrscanner.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanOptions
import ru.ramil.customqrscanner.ui.theme.CustomQrScannerTheme
import ru.ramil.customqrscanner.ui.utils.IScanListener
import ru.ramil.customqrscanner.ui.utils.LaunchScanner
import ru.ramil.customqrscanner.ui.utils.withPermissionCamera
import ru.ramil.customqrscanner.ui.view_models.MainState
import ru.ramil.customqrscanner.ui.view_models.MainViewModel

@Composable
fun MainScreen(
    viewModel : MainViewModel
) {
    val state = viewModel.state.collectAsState()

    when(state.value){
        is MainState.ErrorSaveData -> {
            Toast.makeText(LocalContext.current, "«Ошибка: ${(state.value as MainState.ErrorSaveData).exception}", Toast.LENGTH_SHORT).show()
        }
        MainState.Idle -> {}
        MainState.Loading -> {}
        MainState.RequestPermissionForCustom -> {
            withPermissionCamera {
                viewModel.toScannerActivity()
            }
        }
        is MainState.Scanned -> {
            val content = (state.value as MainState.Scanned).data
            OpenDialog(
                content = content
                ,save = { viewModel.saveDataInRepository(content) },
                _continue = { viewModel.toCustomScanner() },
                onDismiss = { viewModel.clear() })
        }
        MainState.SuccessSaveData -> {
            Toast.makeText(LocalContext.current, "«Данные успешно загружены»", Toast.LENGTH_SHORT).show()
        }
        MainState.SuccessPermission -> {
            OpenScanner(
                onScanning = viewModel::scanData,
                onClose = viewModel::clear)
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun OpenScanner(
    onScanning : (String)->Unit,
    onClose : ()->Unit
){
    CustomQrScannerTheme {
        Scaffold {
            LaunchScanner(
                scanOptions = ScanOptions()
                    .apply {
                        setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                        setPrompt("Scan a qr code")
                        setCameraId(0)
                        setBeepEnabled(false)
                        setBarcodeImageEnabled(false)
                        setOrientationLocked(true)
                    },
                listener = object : IScanListener {
                    override fun cancel() {
                        onClose()
                    }

                    override fun scanData(data: String) {
                        onScanning(data)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpenDialog(
    content : String,
    save : ()->Unit,
    _continue : ()->Unit,
    onDismiss : ()->Unit
) {
    val dismiss = remember {
        mutableStateOf(false)
    }

    when (dismiss.value) {
        true -> {
            onDismiss()
        }

        false -> {
            ModalBottomSheet(
                onDismissRequest = {
                    dismiss.value = true
                    onDismiss()
                },
                content = {
                    Text(
                        text = "Информация в qr code:\n$content",
                        Modifier.align(Alignment.CenterHorizontally)
                    )

                    Row(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(0.dp, 14.dp)
                    ) {
                        Button(onClick = {
                            save()
                            dismiss.value = true
                        }) {
                            Text(text = "Загрузить")
                        }

                        Spacer(Modifier.width(10.dp))

                        Button(onClick = {
                            _continue()
                            dismiss.value = true
                        }) {
                            Text(text = "Отмена")
                        }
                    }
                })
        }
    }
}