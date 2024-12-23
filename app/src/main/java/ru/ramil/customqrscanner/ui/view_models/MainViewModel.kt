package ru.ramil.customqrscanner.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.ramil.customqrscanner.domain.qr.SendQrDataUseCase
import ru.ramil.customqrscanner.domain.request.QrDataModel
import java.sql.SQLException
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sendQrDataUseCase: SendQrDataUseCase
) : ViewModel() {

    private val intentChannel : Channel<MainIntent> = Channel(Channel.CONFLATED)
    private val _state : MutableStateFlow<MainState> =
        MutableStateFlow(MainState.Idle)
    val state : StateFlow<MainState>
        get() = _state

    private var saveJob : Job? = null

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    MainIntent.Idle -> _state.emit(MainState.Idle)
                    MainIntent.RequestCameraForCustom -> _state.emit(MainState.RequestPermissionForCustom)
                    MainIntent.CancelSaveData -> cancelSaveJob()
                    MainIntent.ToScanner -> _state.emit(MainState.SuccessPermission)
                    is MainIntent.SaveData -> {
                        saveDataInRepository(it.qrDataModel)
                    }
                    is MainIntent.ScanData -> _state.emit(MainState.Scanned(it.data))
                }
            }
        }
    }

    private fun saveDataInRepository(qrDataModel: QrDataModel){
        saveJob = viewModelScope.launch {
            _state.emit(MainState.Loading)
            try{
                val success = sendQrDataUseCase.execute(qrDataModel)
                _state.emit(MainState.SuccessSaveData)
            } catch (e : SQLException){
                _state.emit(MainState.ErrorSaveData(e))
            }
        }
    }

    private fun cancelSaveJob(){
        saveJob?.cancel()
    }

    fun saveDataInRepository(content : String){
        event(MainIntent.SaveData(QrDataModel(content)))
    }

    fun cancel(){
        event(MainIntent.CancelSaveData)
    }

    fun scanData(data : String){
        event(MainIntent.ScanData(data))
    }

    fun toScannerActivity(){
        event(MainIntent.ToScanner)
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

    data object CancelSaveData : MainIntent()
    data object ToScanner : MainIntent()

    data class SaveData(val qrDataModel: QrDataModel) : MainIntent()

    data class ScanData(val data : String) : MainIntent()
}

sealed class MainState{

    data object Idle : MainState()

    data object RequestPermissionForCustom : MainState()

    data object  SuccessPermission: MainState()

    data object Loading : MainState()

    data class ErrorSaveData(val exception: Exception) : MainState()

    data object SuccessSaveData : MainState()

    data class Scanned(val data : String) : MainState()
}

/*
sealed class RequestPermission{

    data object Idle : RequestPermission()

    data object RequestPermissionForCustom : RequestPermission()

    data object  Success: RequestPermission()
}

sealed class QrScanningDataState{
    data object Idle : QrScanningDataState()

    data class Scanned(val data : String) : QrScanningDataState()
}

sealed class QrScannerSavableState {
    data object Idle : QrScannerSavableState()

    data object Loading : QrScannerSavableState()

    data class Error(val exception: Exception) : QrScannerSavableState()

    data object Success : QrScannerSavableState()
}*/
