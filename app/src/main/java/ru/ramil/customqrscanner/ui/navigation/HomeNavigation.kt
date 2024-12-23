package ru.ramil.customqrscanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.ramil.customqrscanner.ui.IViewModelByLifeCycleStore
import ru.ramil.customqrscanner.ui.screens.MainScreen
import ru.ramil.customqrscanner.ui.view_models.MainViewModel

class HomeNavAction(navHostController: NavHostController){

    val toMain : () -> Unit = {
        navHostController.toDestination(HomeScreenDestination.Main)
    }

    val onBack : () -> Unit = {
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
        composable<HomeScreenDestination.Main> {
            MainScreen(
                viewModel = viewModelCreator
                    .create(zclass = MainViewModel::class.java)
            )
        }
    }
}

@Serializable
sealed class HomeScreenDestination(val id : String) {

    @Serializable
    data object Main : HomeScreenDestination("Main")
}