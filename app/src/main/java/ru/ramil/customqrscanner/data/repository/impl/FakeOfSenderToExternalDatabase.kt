package ru.ramil.customqrscanner.data.repository.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ramil.customqrscanner.data.models.QrDataEntity
import ru.ramil.customqrscanner.data.repository.ISendQrData
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

// Обычно так организовывается тест: fake - вида для проверки работоспособности.

// тк по тестовому заданию непонятно какая будет база данных внутренняя
// или внешняя - общий интерфейс и реализация fake - такая
class FakeOfSenderToExternalDatabase(
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : ISendQrData, Closeable {

    //not injected
    private val database: FakeDatabase
    private var connection : IDatabaseConnection? = null

    init {
        database = object : FakeDatabase {

            init {
                println("init fake database interface")
            }

            @Volatile
            override var isConnect: Boolean = false
                set(value) {
                    field = value
                }

            override fun getConnection(databaseUrl : String, user : String, password : String): IDatabaseConnection {
                return object : IDatabaseConnection{

                    // Здесь запрос на получение коннекции с базой данных MySql
                    init {
                        println("connect to url $databaseUrl")
                    }

                    override fun transaction(request: IDatabaseRequest): IDatabaseResponse {
                        /*println("database transaction request")
                        println("database transaction response")*/
                        return SimpleResponse
                    }

                    override fun close() {
                        /*println("database connect - close")*/
                    }
                }
            }
        }
    }

    override suspend fun send(qrData: QrDataEntity) : Boolean =
        closeableTransaction {
            true
        }

    private suspend fun connect() = executeWithContext{
        connection = database.getConnection(
            databaseUrl = "jdbc:mysql://ip:port/dataBase",
            user = "user",
            password = "password"
        )
    }

    override fun close() {
        connection?.close()
        connection = null
    }

    private suspend fun <T> closeableTransaction(block : suspend ()->T) : T = transaction {
        val tempValue = block.invoke()
        close()
        tempValue
    }

    private suspend fun <T> transaction(block : suspend ()->T) : T = executeWithContext {
        if (!database.isConnect){
            connect()
        }

        block()
    }

    private suspend fun <T> executeWithContext(block : suspend ()->T) : T = withContext(coroutineContext){
        block()
    }
}
