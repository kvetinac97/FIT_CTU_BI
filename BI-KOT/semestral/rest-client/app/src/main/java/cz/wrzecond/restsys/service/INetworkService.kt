package cz.wrzecond.restsys.service

import cz.wrzecond.restsys.task.HttpRequest
import cz.wrzecond.restsys.task.HttpResponse
import wrzecond.TjvEmployeeReadDTO

/**
 * Interface defining Network methods
 */
interface INetworkService {

    // Properties
    val isLoggedIn   : Boolean
    val isAdmin      : Boolean
    val loggedUserID : Int?

    // Login / logout
    fun login  (dto: TjvEmployeeReadDTO, password: String)
    fun logout ()

    // Perform asynchronous action
    fun scheduleTask (request: HttpRequest, action: (HttpResponse) -> Unit)

}