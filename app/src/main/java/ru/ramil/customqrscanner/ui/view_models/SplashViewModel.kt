package ru.ramil.customqrscanner.ui.view_models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    private val loadingDataMutable : MutableStateFlow<SplashState> = MutableStateFlow(SplashState.Loading)
    val loadingData : StateFlow<SplashState> = loadingDataMutable.asStateFlow()

    init {
        loadingDataMutable.value = SplashState.Success
    }
}

sealed class SplashState{
    data object Loading : SplashState()
    data object Success : SplashState()
    data object Error : SplashState()
}