package ru.ramil.customqrscanner.domain.request

import ru.ramil.customqrscanner.data.models.QrDataEntity

@JvmInline
value class QrDataModel(val information : String)

fun QrDataModel.toQrDataEntity() =
    QrDataEntity(information)