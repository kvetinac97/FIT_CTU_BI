package cz.cvut.fit.caltrack.data.entity

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Generic entity interface
 * (abstract class is used because interfaces cannot have fields)
 */
@MappedSuperclass
abstract class IEntity (
    /** The unique ID of entity */
    @Id
    @GeneratedValue
    open val id: Int = 0
)