package wrzecond.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import wrzecond.ICreateDTO
import wrzecond.IReadDTO
import wrzecond.IUpdateDTO
import wrzecond.entity.TjvEmployee
import wrzecond.entity.TjvEntity
import wrzecond.service.TjvEmployeeService
import wrzecond.service.TjvService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.full.findAnnotation

abstract class TjvController<T: TjvEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO>
    (protected open val service: TjvService<T, R, C, U>, protected val employeeService: TjvEmployeeService) {

    protected abstract val createLocationPath: String

    // === AUTHENTICATION ===
    private fun <A> authenticate (request: HttpServletRequest, function: (Visibility) -> VisibilitySettings, action: (TjvEmployee?) -> A) : A {
        val annotation = this::class.findAnnotation<Visibility>()
        return authenticate(request, function(annotation!!), action)
    }
    protected fun <A> authenticate (request: HttpServletRequest, settings: VisibilitySettings, action: (TjvEmployee?) -> A) : A {
        // Load data from header
        val username = request.getHeader("Username") ?: ""
        val password = request.getHeader("Password") ?: ""

        // Try to auth, exception = not authed
        val employee = try { employeeService.getByUsernamePassword(username, password) }
        catch (e: ResponseStatusException) { null }

        // Throw UNAUTHORIZED/FORBIDDEN if we need to be authed
        when (settings) {
            VisibilitySettings.ALL    -> {}
            VisibilitySettings.LOGGED -> if (employee == null) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
            VisibilitySettings.ADMIN  -> {
                if (employee == null)
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
                if (!employee.admin)
                    throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
            VisibilitySettings.NONE   -> throw ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED)
        }

        // Perform requested action
        return action(employee)
    }

    // Helper auth annotation class
    annotation class Visibility (
        val findAll: VisibilitySettings = VisibilitySettings.ALL,
        val getByID: VisibilitySettings = VisibilitySettings.ALL,
        val create : VisibilitySettings = VisibilitySettings.ADMIN,
        val delete : VisibilitySettings = VisibilitySettings.ADMIN,
        val update : VisibilitySettings = VisibilitySettings.ADMIN
    )
    enum class VisibilitySettings { NONE, ADMIN, LOGGED, ALL }
    // === AUTHENTICATION ===

    @GetMapping
    open fun all (request: HttpServletRequest) = authenticate(request, {it.findAll}) {
        service.findAll()
    }

    @GetMapping("/{id}")
    fun getByID (@PathVariable id: Int, request: HttpServletRequest) = authenticate(request, {it.getByID}) {
        service.getByID(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create (@RequestBody createDTO: C, request: HttpServletRequest, response: HttpServletResponse)
    = authenticate(request, {it.create}) {
        val dto = service.create(createDTO)[0]
        response.addHeader("Location", String.format(createLocationPath, dto.id))
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create (@PathVariable id: Int, @RequestBody updateDTO: U, request: HttpServletRequest)
    = authenticate(request, {it.update}) {
        service.update(id, updateDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete (@PathVariable id: Int, request: HttpServletRequest)
    = authenticate(request, {it.delete}) {
        service.delete(id)
    }

}