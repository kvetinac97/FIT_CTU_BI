package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.Food
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository : IRepository<Food> {

    /**
     *  Finds all food with name containing given name
     *  @param name searched name
     *  @return list of food matching given name
     */
    @Query("SELECT food FROM Food food WHERE food.name LIKE %:name%")
    fun searchByName (name: String) : List<Food>

}