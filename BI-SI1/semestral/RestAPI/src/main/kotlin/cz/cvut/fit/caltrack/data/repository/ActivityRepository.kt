package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.Activity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository : IRepository<Activity> {

    /**
     *  Finds all activities with name containing given name
     *  @param name searched name
     *  @return list of activities matching given name
     */
    @Query("SELECT activity FROM Activity activity WHERE activity.name LIKE %:name%")
    fun searchByName (name: String) : List<Activity>

}