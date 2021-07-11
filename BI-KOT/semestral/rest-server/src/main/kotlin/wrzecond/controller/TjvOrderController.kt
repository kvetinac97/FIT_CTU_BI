package wrzecond.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wrzecond.TjvOrderCreateDTO
import wrzecond.TjvOrderReadDTO
import wrzecond.TjvOrderUpdateDTO
import wrzecond.entity.TjvOrder
import wrzecond.service.TjvEmployeeService
import wrzecond.service.TjvOrderService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/orders/")
@TjvController.Visibility (
    create = TjvController.VisibilitySettings.LOGGED,
    findAll = TjvController.VisibilitySettings.LOGGED,
    update = TjvController.VisibilitySettings.LOGGED,
    delete = TjvController.VisibilitySettings.LOGGED,
    getByID = TjvController.VisibilitySettings.NONE
)
class TjvOrderController (override val service: TjvOrderService, employeeService: TjvEmployeeService)
: TjvController<TjvOrder, TjvOrderReadDTO, TjvOrderCreateDTO, TjvOrderUpdateDTO> (service, employeeService) {

    override val createLocationPath: String
        get() = "/orders/%d"

    @GetMapping("at/{id}")
    fun findByTableID (@PathVariable id: Int, request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.LOGGED) { service.findByTableID(id) }

}