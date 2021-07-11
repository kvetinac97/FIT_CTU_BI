package wrzecond.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wrzecond.TjvTableCreateDTO
import wrzecond.TjvTableReadDTO
import wrzecond.TjvTableUpdateDTO
import wrzecond.entity.TjvTable
import wrzecond.service.TjvEmployeeService
import wrzecond.service.TjvOrderService
import wrzecond.service.TjvTableService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/tables/")
@TjvController.Visibility (
    findAll = TjvController.VisibilitySettings.LOGGED,
    getByID = TjvController.VisibilitySettings.NONE
)
class TjvTableController (override val service: TjvTableService, private val orderService: TjvOrderService, employeeService: TjvEmployeeService)
: TjvController<TjvTable, TjvTableReadDTO, TjvTableCreateDTO, TjvTableUpdateDTO> (service, employeeService) {

    override val createLocationPath: String
        get() = "/tables/%d"

    @GetMapping
    override fun all (request: HttpServletRequest)
        = super.all(request)
            .asSequence()
            .onEach { it.empty = orderService.findByTableID(it.id).isEmpty() }
            .toList()

}