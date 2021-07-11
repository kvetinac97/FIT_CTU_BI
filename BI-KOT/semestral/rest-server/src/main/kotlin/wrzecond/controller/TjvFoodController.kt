package wrzecond.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wrzecond.TjvFoodCreateDTO
import wrzecond.TjvFoodReadDTO
import wrzecond.TjvFoodUpdateDTO
import wrzecond.entity.TjvFood
import wrzecond.service.TjvEmployeeService
import wrzecond.service.TjvFoodService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/food/")
@TjvController.Visibility
class TjvFoodController (override val service: TjvFoodService, employeeService: TjvEmployeeService)
: TjvController<TjvFood, TjvFoodReadDTO, TjvFoodCreateDTO, TjvFoodUpdateDTO> (service, employeeService) {

    override val createLocationPath: String
        get() = "/food/%d"

    @GetMapping("allergens/{id}")
    fun getAllergensInFood (@PathVariable id: Int, request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.ALL) { service.getAllergensInFood(id) }

}