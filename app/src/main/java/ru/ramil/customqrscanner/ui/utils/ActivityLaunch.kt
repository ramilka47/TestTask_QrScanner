package ru.ramil.customqrscanner.ui.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun LaunchScanner(scanOptions : ScanOptions, listener : IScanListener){
    val barcodeLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result->
        val contents = result?.contents
        if (contents?.isNotBlank() == true){
            listener.scanData(contents)
        } else {
            listener.cancel()
        }
    }

    LaunchedEffect(barcodeLauncher) {
        barcodeLauncher.launch(scanOptions)
    }
}

interface IScanListener{
    fun scanData(data : String)

    fun cancel()
}