package ru.ramil.customqrscanner.ui.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ramil.customqrscanner.ui.view_models.ActivityViewModel
import ru.ramil.customqrscanner.ui.view_models.CustomQrScannerViewModel
import ru.ramil.customqrscanner.ui.view_models.HomeViewModel
import ru.ramil.customqrscanner.ui.view_models.MainViewModel
import ru.ramil.customqrscanner.ui.view_models.SplashViewModel
import ru.ramil.customqrscanner.ui.view_models.ViewModelFactory
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    internal abstract fun bindActivityViewModel(activityViewModel: ActivityViewModel) :
            ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomQrScannerViewModel::class)
    internal abstract fun bindCustomQrScannerViewModel(customQrScannerViewModel: CustomQrScannerViewModel) :
            ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel) :
            ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(splashViewModel: SplashViewModel) :
            ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(homeViewModel: HomeViewModel) :
            ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory : ViewModelFactory) :
            ViewModelProvider.Factory
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)