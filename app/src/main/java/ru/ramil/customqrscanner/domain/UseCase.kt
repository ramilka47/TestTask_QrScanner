package ru.ramil.customqrscanner.domain

interface UseCase<Request, Response> {

    suspend fun execute(request: Request) : Response
}

suspend fun <Response> UseCase<Unit, Response>.execute() : Response = execute(Unit)