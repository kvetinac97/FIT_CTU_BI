package wrzecond.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.server.ResponseStatusException
import wrzecond.ICreateDTO
import wrzecond.IReadDTO
import wrzecond.IUpdateDTO
import wrzecond.entity.TjvEntity
import wrzecond.repository.TjvRepository

abstract class TjvService<T: TjvEntity, R: IReadDTO, C: ICreateDTO, U: IUpdateDTO> (open val repository: TjvRepository<T>) {

    /** Default sort */
    protected open val sort: Sort
        get () = Sort.unsorted()

    /** Helper method to get entity with given ID */
    fun getEntityByID  (id: Int) = tryCatch { repository.getOne(id) }

    // Get
    fun getByID (id: Int) = getEntityByID(id).toDTO()
    fun findByIDs (ids: List<Int>) = ids.map(this::getEntityByID)
    fun findAll () = repository.findAll(sort).map { it.toDTO() }

    // Create, Update, Delete
    fun create (vararg dtos: C)         = tryCatch { dtos.map { repository.save(it.toEntity()).toDTO() }.apply { repository.flush() } }
    fun update (id: Int, dto: U) : Unit = tryCatch { repository.saveAndFlush(getEntityByID(id).merge(dto)) }
    open fun delete (id: Int)           = tryCatch { repository.deleteById(id) }

    // Extension function allowing for mapping Entity to Read DTO
    abstract fun T.toDTO() : R

    // Extension function allowing for mapping Create DTO to Entity
    abstract fun C.toEntity() : T

    // Extension function allowing for updating Entity with Update DTO
    abstract fun T.merge(dto: U) : T

    /**
     * Helper extension function allowing to perform repository action,
     * catch any exception and change it to ResponseStatusException
     * @param block action to perform
     * @return block return value
     */
    protected fun <X> tryCatch (block: TjvRepository<T>.() -> X) =
        try { repository.block() }
        catch (_: JpaObjectRetrievalFailureException) { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        catch (_: EmptyResultDataAccessException)     { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        catch (e: Exception) { e.printStackTrace(); throw ResponseStatusException(HttpStatus.BAD_REQUEST) }

}
