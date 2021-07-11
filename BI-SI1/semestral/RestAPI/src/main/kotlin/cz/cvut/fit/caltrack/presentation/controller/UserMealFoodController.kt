package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.UserMealFoodCreateDTO
import cz.cvut.fit.caltrack.business.dto.UserMealFoodReadDTO
import cz.cvut.fit.caltrack.business.dto.UserMealFoodUpdateDTO
import cz.cvut.fit.caltrack.data.entity.UserMealFood
import cz.cvut.fit.caltrack.business.service.UserMealFoodService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Date
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user-food")
class UserMealFoodController (override val service: UserMealFoodService, userService: UserService)
: IControllerImpl<UserMealFood, UserMealFoodReadDTO, UserMealFoodCreateDTO, UserMealFoodUpdateDTO>(service, userService) {

    /**
     *  Finds all food current user ate on given day
     *  @param day day on which the food was eaten
     *  @param request current HTTP request, used to determine current user
     *  @return list of UserMealFoodDTOs of food current user ate on given day in ascending order by meal (breakfast -> lunch -> dinner)
     */
    @GetMapping("/daily/{day}")
    fun getDaily (@PathVariable day: Date, request: HttpServletRequest)
        = authenticate(request) { service.getDaily(it, day) }

    /**
     *  Finds all food current user ate today
     *  @param request current HTTP request, used to determine current user
     *  @return list of UserMealFoodDTOs of food current user ate today in ascending order by meal (breakfast -> lunch -> dinner)
     */
    @GetMapping("/daily")
    fun getDaily (request: HttpServletRequest)
        = authenticate(request) { service.getDaily(it, Date(System.currentTimeMillis())) }

    /**
     *  Finds all food current user has ever eaten, ordered by frequency
     *  @param request current HTTP request, used to determine current user
     *  @return list of FoodReadDTOs of food given current user ate
     */
    @GetMapping("/history")
    fun getHistory (request: HttpServletRequest)
        = authenticate(request) { service.getHistory(it) }

}