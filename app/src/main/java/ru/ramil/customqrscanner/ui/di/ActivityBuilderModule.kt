package ru.ramil.customqrscanner.ui.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.ramil.customqrscanner.ui.MainActivity
import ru.ramil.customqrscanner.ui.di.modules.ViewModelModule

@Module(subcomponents = [
    MainActivitySubComponent::class
])
abstract class ActivityBuilderModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(factory : MainActivitySubComponent.Factory):
            AndroidInjector.Factory<*>
}