package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.UserCreateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.business.dto.UserUpdateDTO
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user")
class UserController (override val service: UserService)
: IControllerImpl<User, UserReadDTO, UserCreateDTO, UserUpdateDTO> (service, service) {

    /**
     * Gets information about user currently logged in
     * @param request current HTTP request, used to determine logged user
     * @throws ResponseStatusException with code 401 if no user is currently logged in
     * @return read DTO of given user
     */
    @GetMapping("/me")
    fun getByCode (request: HttpServletRequest) : UserReadDTO
        = authenticate(request) { service.getByID(it.id) }

    /**
     * Update currently logged user based on given UserUpdateDTO
     * @param dto Update DTO to update current user with
     * @param request HTTP request used to determine logged user
     * @throws ResponseStatusException with code 400 if update DTO is invalid
     * @throws ResponseStatusException with code 404 if entity was not found
     */
    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update (@RequestBody dto: UserUpdateDTO, request: HttpServletRequest)
        = authenticate(request) { service.update(it, it.id, dto); }

    /** Overriding default implementation, banning updating users throughout their ID */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun update (@PathVariable id: Int, @RequestBody dto: UserUpdateDTO, request: HttpServletRequest) {
        throw ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED)
    }

    /** Overriding default implementation, banning deleting users throughout their ID */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete (@PathVariable id: Int, request: HttpServletRequest) {
        throw ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED)
    }

}