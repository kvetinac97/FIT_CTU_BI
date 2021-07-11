package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.Activity
import cz.cvut.fit.caltrack.business.service.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/activity")
class ActivityController (override val service: ActivityService, userService: UserService)
: IControllerImpl<Activity, ActivityReadDTO, ActivityCreateDTO, ActivityUpdateDTO>(service, userService) {

    /**
     * Search for activities matching given name
     * @param name name of activity being searched for
     * @param request current HTTP request, used for authentication
     * @return list of activities matching given name
     */
    @GetMapping("/search/{name}")
    fun findByName (@PathVariable name: String, request: HttpServletRequest) : List<ActivityReadDTO>
        = authenticate(request) { service.searchByName(name) }

}