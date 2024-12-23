package ru.ramil.customqrscanner.ui.di.modules

import dagger.Module
import dagger.Provides
import ru.ramil.customqrscanner.data.repository.ISendQrData
import ru.ramil.customqrscanner.domain.qr.SendQrDataUseCase

@Module
class UseCaseModule {

    @Provides
    fun provideSendQrDataUseCase(iSendQrData: ISendQrData) =
        SendQrDataUseCase(iSendQrData = iSendQrData)
}