package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.IEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Generic repository interface
 * extends JpaRepository, which defines all CRUD operations on entity
 */
interface IRepository<T: IEntity> : JpaRepository<T, Int>