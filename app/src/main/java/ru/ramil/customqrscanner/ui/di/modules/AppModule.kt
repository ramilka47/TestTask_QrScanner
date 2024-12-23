package ru.ramil.customqrscanner.ui.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ramil.customqrscanner.Application

@Module
class AppModule(private val application : Application) {

    @Provides
    fun provideApplication() = application

    @Provides
    fun provideContext() : Context = application
}