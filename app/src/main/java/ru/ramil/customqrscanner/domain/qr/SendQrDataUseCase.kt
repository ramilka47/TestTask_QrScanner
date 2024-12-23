package ru.ramil.customqrscanner.domain.qr

import ru.ramil.customqrscanner.data.models.QrDataEntity
import ru.ramil.customqrscanner.data.repository.ISendQrData
import ru.ramil.customqrscanner.domain.UseCase
import ru.ramil.customqrscanner.domain.request.QrDataModel
import ru.ramil.customqrscanner.domain.request.toQrDataEntity

class SendQrDataUseCase(private val iSendQrData: ISendQrData) : UseCase<QrDataModel, Boolean> {

    override suspend fun execute(request: QrDataModel): Boolean {
        return iSendQrData.send(request.toQrDataEntity())
    }
}