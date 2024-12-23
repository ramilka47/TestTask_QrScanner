package ru.ramil.customqrscanner.data.repository

import ru.ramil.customqrscanner.data.IRepository
import ru.ramil.customqrscanner.data.models.QrDataEntity

interface ISendQrData : IRepository {

    suspend fun send(qrData: QrDataEntity) : Boolean
}