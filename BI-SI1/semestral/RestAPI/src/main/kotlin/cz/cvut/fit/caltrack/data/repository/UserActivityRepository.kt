package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.*
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface UserActivityRepository : IRepository<UserActivity> {

    /**
     *  Finds all activities user has ever performed, ordered by frequency
     *  @param user user who has performed the activities
     *  @return list of activities given user performed
     */
    @Query("SELECT ua.activity FROM UserActivity ua WHERE ua.user = :user GROUP BY ua.activity ORDER BY COUNT(ua.id) DESC")
    fun getHistory (user: User) : List<Activity>

    /**
     *  Finds all activities user performed on given day
     *  @param user user who has performed the activities
     *  @param day day on which the activity was performed
     *  @return list of activities given user performed on given day
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.user = :user AND ua.date = :day")
    fun getDaily (user: User, day: Date) : List<UserActivity>

}