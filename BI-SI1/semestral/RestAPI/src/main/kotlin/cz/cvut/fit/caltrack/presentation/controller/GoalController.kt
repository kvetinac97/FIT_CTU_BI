package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.GoalCreateDTO
import cz.cvut.fit.caltrack.business.dto.GoalReadDTO
import cz.cvut.fit.caltrack.business.dto.GoalUpdateDTO
import cz.cvut.fit.caltrack.data.entity.Goal
import cz.cvut.fit.caltrack.business.service.GoalService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/goal")
class GoalController (override val service: GoalService, userService: UserService)
    : IControllerImpl<Goal, GoalReadDTO, GoalCreateDTO, GoalUpdateDTO>(service, userService) {

    /** Overriding of default implementation, allows to see all goals without being authenticated */
    override fun all (request: HttpServletRequest) = service.findAll()

}