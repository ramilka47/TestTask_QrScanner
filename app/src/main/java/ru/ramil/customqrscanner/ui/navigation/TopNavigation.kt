package ru.ramil.customqrscanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.ramil.customqrscanner.ui.IViewModelByLifeCycleStore
import ru.ramil.customqrscanner.ui.screens.HomeScreen
import ru.ramil.customqrscanner.ui.screens.SplashScreen
import ru.ramil.customqrscanner.ui.view_models.HomeViewModel
import ru.ramil.customqrscanner.ui.view_models.SplashViewModel

@Serializable
sealed class InitDestination(val id : String){

    @Serializable
    data object Splash : InitDestination("Splash")

    @Serializable
    data object Home : InitDestination("Home")
}

class InitNavActions(navController: NavHostController){

    val toSplash : () -> Unit = {
        navController.root(InitDestination.Splash)
    }

    val toHome : () -> Unit = {
        navController.root(InitDestination.Home)
    }

    val onBack : () -> Unit = {
        navController.navigateUp()
    }

    private fun NavHostController.root(destination: InitDestination) = run {
        navigate(destination){
            restoreState = true
            launchSingleTop = true

            popUpTo(requireNotNull(graph.findStartDestination().parent?.id)){
                saveState = true
            }
        }
    }

    private fun NavHostController.screen(destination: InitDestination) = run {
        navigate(destination){
            restoreState = true
        }
    }
}

@Composable
fun InitNavActions.InitNavGraph(
    navHostController: NavHostController,
    viewModelCreator: IViewModelByLifeCycleStore,
    startDestination: InitDestination = InitDestination.Splash) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<InitDestination.Splash> {
            SplashScreen(
                viewModelCreator.create(SplashViewModel::class.java),
                onEndLoad = {
                    toHome()
                }
            )
        }
        composable<InitDestination.Home> {
            HomeScreen(
                viewModelCreator.create(HomeViewModel::class.java),
                viewModelCreator)
        }
    }
}
