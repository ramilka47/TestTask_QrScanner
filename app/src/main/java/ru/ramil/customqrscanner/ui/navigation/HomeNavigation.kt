package ru.ramil.customqrscanner.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import kotlinx.serialization.Serializable
import ru.ramil.customqrscanner.ui.IViewModelByLifeCycleStore
import ru.ramil.customqrscanner.ui.screens.CustomQrScannerScreen
import ru.ramil.customqrscanner.ui.screens.MainScreen
import ru.ramil.customqrscanner.ui.view_models.CustomQrScannerViewModel
import ru.ramil.customqrscanner.ui.view_models.MainViewModel

class HomeNavAction(navHostController: NavHostController){

    val toMain : () -> Unit = {
        navHostController.toDestination(HomeScreenDestination.Main)
    }

    val toCustomScanner : () -> Unit = {
        navHostController.toDestination(HomeScreenDestination.CustomScanner)
    }

    @SuppressLint("RestrictedApi")
    val onBack : () -> Unit = {
        val backStack = navHostController.currentBackStack.value
        navHostController.navigateUp()
    }

    private fun NavHostController.toDestination(destination: HomeScreenDestination) = run {
        navigate(destination){
            restoreState = true
        }
    }
}

@Composable
fun HomeNavAction.HomeNavigationGraph(
    navHostController: NavHostController,
    viewModelCreator: IViewModelByLifeCycleStore,
    startDestination : HomeScreenDestination = HomeScreenDestination.Main) {
    NavHost(navController = navHostController, startDestination = startDestination){
        composable<HomeScreenDestination.CustomScanner> {
            CustomQrScannerScreen(
                viewModel = viewModelCreator
                    .create(zclass = CustomQrScannerViewModel::class.java),
                onBack = {
                    onBack()
                })
        }
        composable<HomeScreenDestination.Main> {
            MainScreen(
                viewModel = viewModelCreator
                    .create(zclass = MainViewModel::class.java),
                toCustomScanner = toCustomScanner
            )
        }
    }
}

@Serializable
sealed class HomeScreenDestination(val id : String) {

    @Serializable
    data object CustomScanner : HomeScreenDestination("CustomQr")

    @Serializable
    data object Main : HomeScreenDestination("Main")
}