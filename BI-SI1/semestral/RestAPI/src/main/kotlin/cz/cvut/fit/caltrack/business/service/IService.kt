package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.ICreateDTO
import cz.cvut.fit.caltrack.business.dto.IReadDTO
import cz.cvut.fit.caltrack.business.dto.IUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.IEntity
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.data.repository.IRepository
import org.springframework.data.domain.Sort
import org.springframework.web.server.ResponseStatusException

/**
 * Generic interface for service
 * contains definition of basic CRUD operations
 */
interface IService<T: IEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO> {

    /** Repository performing CRUD operations on service entity */
    val repository: IRepository<T>

    /** Default way of sorting data in CRUD get methods */
    val sort: Sort

    /**
     * Gets Read DTO of entity with given ID
     * @param id ID of entity
     * @throws ResponseStatusException with code 404 if entity was not found
     * @return Read DTO of entity with given ID
     */
    fun getByID (id: Int) : R

    /**
     * Find entities with given IDs
     * If entity with given ID is not found, it is not added to list
     * @return list of entities with given ids
     */
    fun findByIDs (ids: List<Int>) : List<T>

    /**
     * Find all entities in database
     * @return list of Read DTO reflecting all entities
     */
    fun findAll () : List<R>

    /**
     * Create entities based on given create DTOs
     * @param user actual user performing request
     * @param dtos list of Create DTOs to create entities
     * @throws ResponseStatusException with code 400 if create DTOs are invalid
     * @return list of Read DTOs of created entities
     */
    fun create (user: UserReadDTO, vararg dtos: C) : List<R>

    /**
     * Update entity based on given update DTO
     * @param user actual user performing request
     * @param id ID of entity being updated
     * @param dto Update DTO to update entity with
     * @throws ResponseStatusException with code 400 if create DTOs are invalid
     * @throws ResponseStatusException with code 404 if entity was not found
     */
    fun update (user: UserReadDTO, id: Int, dto: U)

    /**
     * Delete entity with given ID
     * @param id ID of entity to delete
     * @throws ResponseStatusException with code 404 if entity was not found
     */
    fun delete (id: Int)

    /**
     * Extension function allowing for mapping Entity to Read DTO
     * @return Read DTO reflecting given entity
     */
    fun T.toDTO() : R

    /**
     * Extension function allowing for mapping Create DTO to Entity
     * @param user actual user performing request
     * @return Entity containing all data from given Create DTO
     */
    fun C.toEntity (user: UserReadDTO) : T

    /**
     * Extension function allowing for updating Entity with Update DTO
     * For each field of Update DTO, if it is NULL, it should not be updated
     * @param dto Update DTO
     * @param user actual user performing request
     * @return Entity updated with all data from given Update DTO
     */
    fun T.merge(dto: U, user: UserReadDTO) : T

    companion object {
        /** Static property containing basic page size for paged requests */
        const val PAGE_SIZE = 30
    }

}