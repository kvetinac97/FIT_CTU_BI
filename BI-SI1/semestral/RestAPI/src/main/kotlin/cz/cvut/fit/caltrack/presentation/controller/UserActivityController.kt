package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.UserActivity
import cz.cvut.fit.caltrack.business.service.*
import org.springframework.web.bind.annotation.*
import java.sql.Date
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user-activity")
class UserActivityController (override val service: UserActivityService, userService: UserService)
: IControllerImpl<UserActivity, UserActivityReadDTO, UserActivityCreateDTO, UserActivityUpdateDTO>(service, userService) {

    /**
     *  Finds all activities current user performed on given day
     *  @param day day on which the activity was performed
     *  @param request current HTTP request, used to determine current user
     *  @return list of UserActivityReadDTOs of activities current user performed on given day
     */
    @GetMapping("/daily/{day}")
    fun getDaily (@PathVariable day: Date, request: HttpServletRequest) : List<UserActivityReadDTO>
        = authenticate(request) { service.getDaily(it, day) }

    /**
     *  Finds all activities current user performed today
     *  @param request current HTTP request, used to determine current user
     *  @return list of UserActivityReadDTOs of activities given current user performed today
     */
    @GetMapping("/daily")
    fun getDaily (request: HttpServletRequest) : List<UserActivityReadDTO>
        = authenticate(request) { service.getDaily(it, Date(System.currentTimeMillis())) }

    /**
     *  Finds all activities user has ever performed, ordered by frequency
     *  @param request current HTTP request, used to determine current user
     *  @return list of ActivityReadDTOs of activities given user performed
     */
    @GetMapping("/history")
    fun getHistory (request: HttpServletRequest) : List<ActivityReadDTO>
        = authenticate(request) { service.getHistory(it) }

}