package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : IRepository<User> {

    /**
     * Gets the user with given code
     * @param code code of searched user
     * @throws JpaObjectRetrievalFailureException if no user with given code exists
     * @return searched user
     */
    @Query("SELECT u FROM User u WHERE u.code = :code")
    fun getByCode (code: String) : User

}