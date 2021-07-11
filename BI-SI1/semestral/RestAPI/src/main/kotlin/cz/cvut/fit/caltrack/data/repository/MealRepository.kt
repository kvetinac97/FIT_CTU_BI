package cz.cvut.fit.caltrack.data.repository

import cz.cvut.fit.caltrack.data.entity.Meal
import org.springframework.stereotype.Repository

@Repository
interface MealRepository : IRepository<Meal>