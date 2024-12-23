package ru.ramil.customqrscanner.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.ramil.customqrscanner.ui.IViewModelByLifeCycleStore
import ru.ramil.customqrscanner.ui.navigation.HomeNavAction
import ru.ramil.customqrscanner.ui.navigation.HomeNavigationGraph
import ru.ramil.customqrscanner.ui.theme.CustomQrScannerTheme
import ru.ramil.customqrscanner.ui.view_models.HomeViewModel

@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    viewModelCreator : IViewModelByLifeCycleStore
){
    CustomQrScannerTheme {
        val navController = rememberNavController()
        val actionController = HomeNavAction(navController)
        actionController.HomeNavigationGraph(
            navHostController = navController,
            viewModelCreator = viewModelCreator)
    }
}