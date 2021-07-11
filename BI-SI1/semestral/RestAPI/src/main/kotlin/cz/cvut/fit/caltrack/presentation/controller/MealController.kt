package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.MealCreateDTO
import cz.cvut.fit.caltrack.business.dto.MealReadDTO
import cz.cvut.fit.caltrack.business.dto.MealUpdateDTO
import cz.cvut.fit.caltrack.data.entity.Meal
import cz.cvut.fit.caltrack.business.service.MealService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/meal")
class MealController (override val service: MealService, userService: UserService)
    : IControllerImpl<Meal, MealReadDTO, MealCreateDTO, MealUpdateDTO>(service, userService)