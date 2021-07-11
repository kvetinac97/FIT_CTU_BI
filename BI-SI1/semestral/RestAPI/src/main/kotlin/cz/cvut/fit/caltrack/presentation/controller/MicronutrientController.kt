package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.MicronutrientCreateDTO
import cz.cvut.fit.caltrack.business.dto.MicronutrientReadDTO
import cz.cvut.fit.caltrack.business.dto.MicronutrientUpdateDTO
import cz.cvut.fit.caltrack.data.entity.Micronutrient
import cz.cvut.fit.caltrack.business.service.MicronutrientService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/micronutrient")
class MicronutrientController (override val service: MicronutrientService, userService: UserService)
    : IControllerImpl<Micronutrient, MicronutrientReadDTO, MicronutrientCreateDTO, MicronutrientUpdateDTO>(service, userService)