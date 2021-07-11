package wrzecond.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import wrzecond.TjvEmployeeCreateDTO
import wrzecond.TjvEmployeeReadDTO
import wrzecond.TjvEmployeeUpdateDTO
import wrzecond.TjvPasswordDTO
import wrzecond.entity.TjvEmployee
import wrzecond.service.TjvEmployeeService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/employees")
@TjvController.Visibility (
    findAll = TjvController.VisibilitySettings.ADMIN,
    getByID = TjvController.VisibilitySettings.ADMIN
)
class TjvEmployeeController (override val service: TjvEmployeeService)
: TjvController<TjvEmployee, TjvEmployeeReadDTO, TjvEmployeeCreateDTO, TjvEmployeeUpdateDTO> (service, service) {

    override val createLocationPath: String
        get() = "/employees/%d"

    @PatchMapping("/passChange")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun passChange (@RequestBody dto: TjvPasswordDTO, request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.LOGGED) { employee: TjvEmployee? ->
            if (employee == null) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
            service.updatePassword(employee, dto.old, dto.new)
        }

    @GetMapping("/login")
    fun login (request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.LOGGED) { employee: TjvEmployee? ->
            if (employee == null) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
            service.getByID(employee.id)
        }

}