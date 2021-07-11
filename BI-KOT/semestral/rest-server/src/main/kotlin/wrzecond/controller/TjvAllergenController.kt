package wrzecond.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wrzecond.TjvAllergenCreateDTO
import wrzecond.TjvAllergenReadDTO
import wrzecond.TjvAllergenUpdateDTO
import wrzecond.entity.TjvAllergen
import wrzecond.service.TjvAllergenService
import wrzecond.service.TjvEmployeeService

@RestController
@RequestMapping("/allergens")
@TjvController.Visibility (
    getByID = TjvController.VisibilitySettings.NONE
)
class TjvAllergenController (override val service: TjvAllergenService, employeeService: TjvEmployeeService)
: TjvController<TjvAllergen, TjvAllergenReadDTO, TjvAllergenCreateDTO, TjvAllergenUpdateDTO> (service, employeeService) {
    override val createLocationPath: String
        get() = "/allergens/%d"
}