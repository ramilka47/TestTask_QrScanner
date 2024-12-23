package ru.ramil.customqrscanner.ui.utils

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionCamera(
    vararg permissions : String,
    onPermissionGranted : (String)->Unit,
    onPermissionDenied : (String)->Unit
){
    val cameraPermissionState = rememberMultiplePermissionsState(permissions = permissions.toList())

    cameraPermissionState.permissions.forEach {
        if (it.hasPermission){
            onPermissionGranted(it.permission)
        } else {
            SideEffect {
                it.launchPermissionRequest()
            }
        }
    }
}

@Composable
fun withPermissionCamera(block : ()->Unit){
    PermissionCamera(
        permissions = arrayOf( Manifest.permission.CAMERA ),
        onPermissionGranted = {
            block()
        },
        onPermissionDenied = {})
}