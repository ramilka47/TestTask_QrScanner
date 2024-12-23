package ru.ramil.customqrscanner.ui.di.modules

import dagger.Module
import dagger.Provides
import ru.ramil.customqrscanner.data.repository.ISendQrData
import ru.ramil.customqrscanner.data.repository.impl.FakeOfSenderToExternalDatabase
import javax.inject.Singleton

@Module
class ProviderModule {

    @Singleton
    @Provides
    fun provideISendQrData() : ISendQrData =
        FakeOfSenderToExternalDatabase()
}