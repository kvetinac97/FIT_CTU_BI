package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.ICreateDTO
import cz.cvut.fit.caltrack.business.dto.IReadDTO
import cz.cvut.fit.caltrack.business.dto.IUpdateDTO
import cz.cvut.fit.caltrack.business.dto.UserReadDTO
import cz.cvut.fit.caltrack.data.entity.IEntity
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.data.repository.IRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.server.ResponseStatusException

abstract class IServiceImpl<T: IEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO> (override val repository: IRepository<T>) : IService<T, R, C, U> {

    /** Default sort */
    override val sort: Sort
        get () = Sort.unsorted()

    /** Helper method to get entity with given ID */
    private fun getEntityByID  (id: Int) = tryCatch { repository.getOne(id) }

    // === Default implementation of interface methods ===

    // Get
    override fun getByID (id: Int) = getEntityByID(id).toDTO()
    override fun findByIDs (ids: List<Int>) = ids.map(this::getEntityByID)
    override fun findAll () = repository.findAll(sort).map { it.toDTO() }

    // Create, Update, Delete
    override fun create (user: UserReadDTO, vararg dtos: C)         = tryCatch { dtos.map { repository.save(it.toEntity(user)).toDTO() }.apply { repository.flush() } }
    override fun update (user: UserReadDTO, id: Int, dto: U) : Unit = tryCatch { repository.saveAndFlush(getEntityByID(id).merge(dto, user)) }
    override fun delete (id: Int)                                   = tryCatch { repository.deleteById(id) }

    // === Default implementation of interface methods ===

    /**
     * Helper template function allowing to perform repository action,
     * catch any exception and change it to ResponseStatusException
     * @param block action to perform
     * @return block return value
     */
    private fun <X> tryCatch (block: IRepository<T>.() -> X) =
        try { repository.block() }
        catch (_: JpaObjectRetrievalFailureException) { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        catch (_: EmptyResultDataAccessException)     { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        catch (_: Exception) { throw ResponseStatusException(HttpStatus.BAD_REQUEST) }

}