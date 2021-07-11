package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.Food
import cz.cvut.fit.caltrack.data.entity.User
import cz.cvut.fit.caltrack.data.entity.UserMealFood
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface UserMealFoodRepository : IRepository<UserMealFood> {

    /**
     *  Finds all food user has ever eaten, ordered by frequency
     *  @param user user who has eaten the food
     *  @return list of food given user ate
     */
    @Query("SELECT umf.food FROM UserMealFood umf WHERE umf.user = :user GROUP BY umf.food ORDER BY COUNT(umf.id) DESC")
    fun getHistory (user: User) : List<Food>

    /**
     *  Finds all food user ate on given day
     *  @param user user who has ate the food
     *  @param day day on which the food was eaten
     *  @return list of food given user ate on given day in ascending order by meal (breakfast -> lunch -> dinner)
     */
    @Query("SELECT umf FROM UserMealFood umf WHERE umf.user = :user AND umf.date = :day ORDER BY umf.meal.timeOfDay")
    fun getDaily (user: User, day: Date) : List<UserMealFood>

}