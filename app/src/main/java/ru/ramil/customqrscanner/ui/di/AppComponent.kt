package ru.ramil.customqrscanner.ui.di

import androidx.lifecycle.ViewModel
import dagger.Component
import dagger.android.AndroidInjectionModule
import ru.ramil.customqrscanner.Application
import ru.ramil.customqrscanner.ui.di.modules.AppModule
import ru.ramil.customqrscanner.ui.di.modules.ProviderModule
import ru.ramil.customqrscanner.ui.di.modules.UseCaseModule
import ru.ramil.customqrscanner.ui.di.modules.ViewModelModule
import javax.inject.Provider
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ProviderModule::class,
    UseCaseModule::class,
    ActivityBuilderModule::class,
    ViewModelModule::class
])
@Singleton
interface AppComponent {

    fun myViewModelMap() : Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>

    fun inject(application: Application)
}