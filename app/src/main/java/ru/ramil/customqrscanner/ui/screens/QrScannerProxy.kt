package ru.ramil.customqrscanner.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanOptions
import ru.ramil.customqrscanner.ui.theme.CustomQrScannerTheme
import ru.ramil.customqrscanner.ui.utils.IScanListener
import ru.ramil.customqrscanner.ui.utils.LaunchScanner
import ru.ramil.customqrscanner.ui.view_models.CustomQrScannerViewModel
import ru.ramil.customqrscanner.ui.view_models.QrScannerSavableState
import ru.ramil.customqrscanner.ui.view_models.QrScannerScreenState
import ru.ramil.customqrscanner.ui.view_models.QrScanningDataState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomQrScannerScreen(
    viewModel : CustomQrScannerViewModel,
    onBack : ()->Unit
){
    val state = viewModel.scannerState.collectAsState()

    when(state.value){
        QrScannerScreenState.Exit -> {
            onBack()
        }
        QrScannerScreenState.Scanning -> {
            OpenScanner(
                onScanning = viewModel::scanData,
                onClose = viewModel::close)
        }
        QrScannerScreenState.Idle -> {
        }
    }

    val scanningState = viewModel.scanningDataState.collectAsState()

    when(scanningState.value) {
        QrScanningDataState.Idle -> {}
        is QrScanningDataState.Scanned -> {
            val content = (scanningState.value as QrScanningDataState.Scanned).data
            OpenDialog(
                content = content
                ,save = { viewModel.saveDataInRepository(content) },
                _continue = { viewModel.continueScanning() },
                onBack = onBack)
        }
    }

    val savableData = viewModel.savableState.collectAsState()

    when(savableData.value){
        is QrScannerSavableState.Error -> {
            Toast.makeText(LocalContext.current, "«Ошибка: ${(savableData.value as QrScannerSavableState.Error).exception}", Toast.LENGTH_SHORT).show()
            onBack()
        }
        QrScannerSavableState.Idle -> {}
        QrScannerSavableState.Loading -> {}
        QrScannerSavableState.Success -> {
            Toast.makeText(LocalContext.current, "«Данные успешно загружены»", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }
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
    onBack: () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = { onBack() },
        content = {
            Text(text = content, Modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(10.dp))

            Row(Modifier.align(Alignment.CenterHorizontally)) {
                Button(onClick = { save() }) {
                    Text(text = "Загрузить")
                }

                Spacer(Modifier.width(10.dp))

                Button(onClick = { _continue() }) {
                    Text(text = "Отмена")
                }
            }
        })
}