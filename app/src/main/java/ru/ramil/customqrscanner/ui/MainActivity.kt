package ru.ramil.customqrscanner.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.android.AndroidInjection
import ru.ramil.customqrscanner.ui.navigation.InitNavActions
import ru.ramil.customqrscanner.ui.navigation.InitNavGraph
import ru.ramil.customqrscanner.ui.view_models.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity(), IViewModelByLifeCycleStore {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MainLine()
        }
    }

    @Composable
    private fun MainLine(){
        val navController = rememberNavController()
        val navigation = InitNavActions(navController)

        navigation.InitNavGraph(
            navHostController = navController,
            viewModelCreator = this)
    }

    @Composable
    override fun <T : ViewModel> create(zclass: Class<T>): T {
        /*return viewModel(
            modelClass = zclass,
            viewModelStoreOwner = this@MainActivity,
            factory = viewModelFactory)*/
        return viewModelFactory.create(zclass)
    }
}

interface IViewModelByLifeCycleStore {

    @Composable
    fun <T : ViewModel> create(zclass : Class<T>) : T
}