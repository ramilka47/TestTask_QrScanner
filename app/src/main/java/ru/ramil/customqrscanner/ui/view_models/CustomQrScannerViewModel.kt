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

class CustomQrScannerViewModel
@Inject constructor(
    private val sendQrDataUseCase: SendQrDataUseCase
) : ViewModel() {

    private val mainIntent = Channel<QrScannerIntent>(Channel.UNLIMITED)
    private val _scannerState : MutableStateFlow<QrScannerScreenState> =
        MutableStateFlow(QrScannerScreenState.Scanning)
    val scannerState: StateFlow<QrScannerScreenState>
        get() = _scannerState

    private val _scanningDataState : MutableStateFlow<QrScanningDataState> =
        MutableStateFlow(QrScanningDataState.Idle)
    val scanningDataState : StateFlow<QrScanningDataState>
        get() = _scanningDataState

    private val _savableDataState : MutableStateFlow<QrScannerSavableState> =
        MutableStateFlow(QrScannerSavableState.Idle)
    val savableState : StateFlow<QrScannerSavableState>
        get() = _savableDataState

    init {
        handleIntent()
    }

    private var saveJob : Job? = null

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    QrScannerIntent.Idle -> {
                        _scannerState.emit(QrScannerScreenState.Idle)
                        _scanningDataState.emit(QrScanningDataState.Idle)
                        _savableDataState.emit(QrScannerSavableState.Idle)
                    }
                    QrScannerIntent.Scanning ->
                        _scannerState.emit(QrScannerScreenState.Scanning)
                    QrScannerIntent.Exit -> {
                        _scanningDataState.emit(QrScanningDataState.Idle)
                        _savableDataState.emit(QrScannerSavableState.Idle)
                        _scannerState.emit(QrScannerScreenState.Exit)
                    }

                    QrScannerIntent.CancelSaveData -> {
                        cancelSaveJob()
                        mainIntent.send(QrScannerIntent.Idle)
                    }
                    is QrScannerIntent.SaveData -> saveDataInRepository(it.qrDataModel)

                    is QrScannerIntent.ScanData -> _scanningDataState.emit(QrScanningDataState.Scanned(it.data))
                }
            }
        }
    }

    fun cancel(){
        event(QrScannerIntent.CancelSaveData)
    }

    fun clear(){
        event(QrScannerIntent.Idle)
    }

    fun scanData(data : String){
        event(QrScannerIntent.ScanData(data))
    }

    fun close(){
        event(QrScannerIntent.Exit)
    }

    fun saveDataInRepository(content : String){
        event(QrScannerIntent.SaveData(QrDataModel(content)))
    }

    fun continueScanning() {
        event(QrScannerIntent.Scanning)
    }

    private fun event(event : QrScannerIntent){
        viewModelScope.launch {
            mainIntent.send(event)
        }
    }

    private fun saveDataInRepository(qrDataModel: QrDataModel){
        saveJob = viewModelScope.launch {
            _savableDataState.emit(QrScannerSavableState.Loading)
            try{
                val success = sendQrDataUseCase.execute(qrDataModel)
                _savableDataState.emit(QrScannerSavableState.Success)
            } catch (e : SQLException){
                _savableDataState.emit(QrScannerSavableState.Error(e))
            }
        }
    }

    private fun cancelSaveJob(){
        saveJob?.cancel()
    }
}

sealed class QrScannerIntent {

    data object Idle : QrScannerIntent()

    data object Exit : QrScannerIntent()

    data object Scanning : QrScannerIntent()

    data object CancelSaveData : QrScannerIntent()

    data class SaveData(val qrDataModel: QrDataModel) : QrScannerIntent()

    data class ScanData(val data : String) : QrScannerIntent()
}

sealed class QrScannerScreenState {
    data object Scanning : QrScannerScreenState()

    data object Exit : QrScannerScreenState()

    data object Idle : QrScannerScreenState()
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
}