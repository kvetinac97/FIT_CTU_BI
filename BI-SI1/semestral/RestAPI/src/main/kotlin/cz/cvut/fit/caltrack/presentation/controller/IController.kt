package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.ICreateDTO
import cz.cvut.fit.caltrack.business.dto.IReadDTO
import cz.cvut.fit.caltrack.business.dto.IUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.IEntity
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.business.service.IService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Generic controller interface
 * automatically creates CRUD methods and maps them
 */
interface IController<T: IEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO> {

    /** Service performing all logic on given DTOs */
    val service: IService<T, R, C, U>

    /** User service, in charge of authentication */
    val userService: UserService

    /**
     * Method used for user authentication from given HTTP request
     * @param request HttpRequest to authenticate from
     * @param block action to be performed if authentication is successful
     * @throws ResponseStatusException with code 401 if authentication failed
     * @return result of performed action
     */
    fun <T> authenticate (request: HttpServletRequest, block: (UserReadDTO) -> T) : T

    /**
     * Find all entities
     * @return list of Read DTO reflecting all entities
     */
    @GetMapping
    fun all (request: HttpServletRequest) : List<R>

    /**
     * Gets ReadDTO of entity with given ID
     * @param id ID of entity
     * @param request HTTP request, could be used for authentication
     * @throws ResponseStatusException with code 404 if entity was not found
     * @return Read DTO of entity with given ID
     */
    @GetMapping("/{id}")
    fun getByID (@PathVariable id: Int, request: HttpServletRequest) : R

    /**
     * Create entity based on given create DTO
     * @param dto Create DTOs to create entity
     * @param request HTTP request, could be used for authentication
     * @param response HTTP response. On success, 'Location' header with created entity ID is added
     * @throws ResponseStatusException with code 400 if create DTO is invalid
     * @return list of Read DTOs of created entities
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create (@RequestBody dto: C, request: HttpServletRequest, response: HttpServletResponse)

    /**
     * Update entity based on given update DTO
     * @param id ID of entity being updated
     * @param dto Update DTO to update entity with
     * @param request HTTP request, could be used for authentication
     * @throws ResponseStatusException with code 400 if create DTOs are invalid
     * @throws ResponseStatusException with code 404 if entity was not found
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update (@PathVariable id: Int, @RequestBody dto: U, request: HttpServletRequest)

    /**
     * Delete entity with given ID
     * @param id ID of entity to delete
     * @throws ResponseStatusException with code 404 if entity was not found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete (@PathVariable id: Int, request: HttpServletRequest)

}
