package ru.ramil.customqrscanner.data.repository.impl

import java.io.Closeable
import java.sql.SQLException
import java.sql.SQLNonTransientException
import kotlin.jvm.Throws


internal interface FakeDatabase {

    var isConnect : Boolean

    @Throws(FakeDatabaseConnectException::class)
    fun getConnection(databaseUrl : String, user : String, password : String) : IDatabaseConnection
}

internal interface IDatabaseConnection : Closeable {

    @Throws(FakeDatabaseTransientException::class)
    fun transaction(request : IDatabaseRequest) : IDatabaseResponse
}

internal interface IDatabaseRequest

internal interface IDatabaseResponse

internal object SimpleResponse : IDatabaseResponse

class FakeDatabaseConnectException(name : String) : SQLException("error connection to database $name")

class FakeDatabaseTransientException : SQLNonTransientException("operation error")