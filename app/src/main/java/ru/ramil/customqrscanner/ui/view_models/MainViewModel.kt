package ru.ramil.customqrscanner.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val intentChannel : Channel<MainIntent> = Channel(Channel.CONFLATED)
    private val _state : MutableStateFlow<RequestPermission> =
        MutableStateFlow(RequestPermission.Idle)
    val state: StateFlow<RequestPermission>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    MainIntent.Idle -> _state.emit(RequestPermission.Idle)
                    MainIntent.RequestCameraForCustom -> _state.emit(RequestPermission.RequestPermissionForCustom)
                }
            }
        }
    }

    fun toCustomScanner(){
        event(MainIntent.RequestCameraForCustom)
    }

    fun clear(){
        event(MainIntent.Idle)
    }

    private fun event(mainIntent: MainIntent){
        viewModelScope.launch {
            intentChannel.send(mainIntent)
        }
    }
}

sealed class MainIntent {

    data object Idle : MainIntent()

    data object RequestCameraForCustom : MainIntent()
}

sealed class RequestPermission{

    data object Idle : RequestPermission()

    data object RequestPermissionForCustom : RequestPermission()
}