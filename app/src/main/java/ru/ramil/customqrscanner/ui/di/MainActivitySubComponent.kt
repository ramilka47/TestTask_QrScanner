package ru.ramil.customqrscanner.ui.di

import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.ramil.customqrscanner.ui.MainActivity
import javax.inject.Scope

@ActivityScope
@Subcomponent(modules = [
    AndroidInjectionModule::class]
)
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope