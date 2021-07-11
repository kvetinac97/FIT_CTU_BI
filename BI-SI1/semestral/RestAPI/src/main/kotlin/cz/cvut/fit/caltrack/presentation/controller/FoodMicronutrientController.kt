package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.UserMealFood
import cz.cvut.fit.caltrack.business.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/food-micronutrient")
class FoodMicronutrientController (override val service: UserMealFoodService, userService: UserService)
: IControllerImpl<UserMealFood, UserMealFoodReadDTO, UserMealFoodCreateDTO, UserMealFoodUpdateDTO>(service, userService)