package ru.ramil.customqrscanner

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.ramil.customqrscanner.ui.di.AppComponent
import ru.ramil.customqrscanner.ui.di.DaggerAppComponent
import ru.ramil.customqrscanner.ui.di.modules.AppModule
import javax.inject.Inject

class Application : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingActivityInjector : DispatchingAndroidInjector<Any>

    companion object{
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> =
        dispatchingActivityInjector
}