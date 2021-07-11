package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.ICreateDTO
import cz.cvut.fit.caltrack.business.dto.IReadDTO
import cz.cvut.fit.caltrack.business.dto.IUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.IEntity
import cz.cvut.fit.caltrack.business.service.IService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Basic controller implementation
 * allows all CRUD methods (when authed)
 */
abstract class IControllerImpl<T: IEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO>
    (override val service: IService<T, R, C, U>, override val userService: UserService) : IController<T, R, C, U> {

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun <T> authenticate (request: HttpServletRequest, block: (UserReadDTO) -> T) : T {
        val code = request.getHeader("Code") ?: ""
        try { return block(userService.getByCode(code)) }
        catch (pass: ResponseStatusException) { throw pass } // rethrow
        catch (exception: Exception) {
            println("Tried auth with $code, got $exception")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping
    override fun all (request: HttpServletRequest)
        = authenticate(request) { service.findAll() }

    @GetMapping("/{id}")
    override fun getByID (@PathVariable id: Int, request: HttpServletRequest)
        = authenticate(request) { service.getByID(id) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create (@RequestBody dto: C, request: HttpServletRequest, response: HttpServletResponse) = authenticate(request) {
        user -> service.create(user, dto).firstOrNull()?.let { response.addHeader("Location", "${it.id}") } ?: Unit
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun update (@PathVariable id: Int, @RequestBody dto: U, request: HttpServletRequest)
        = authenticate(request) { service.update(it, id, dto) }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete (@PathVariable id: Int, request: HttpServletRequest)
        = authenticate(request) { service.delete(id) }

    // === INTERFACE METHOD IMPLEMENTATION ===

}
