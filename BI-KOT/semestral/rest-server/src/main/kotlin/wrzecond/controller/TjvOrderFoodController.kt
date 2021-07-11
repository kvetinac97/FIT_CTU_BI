package wrzecond.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import wrzecond.*
import wrzecond.entity.TjvOrderFood
import wrzecond.service.TjvEmployeeService
import wrzecond.service.TjvOrderFoodService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/order-food/")
@TjvController.Visibility (
    create = TjvController.VisibilitySettings.LOGGED,
    delete = TjvController.VisibilitySettings.LOGGED,
    update = TjvController.VisibilitySettings.LOGGED,
    findAll = TjvController.VisibilitySettings.NONE,
    getByID = TjvController.VisibilitySettings.NONE
)
class TjvOrderFoodController (override val service: TjvOrderFoodService, employeeService: TjvEmployeeService)
: TjvController<TjvOrderFood, TjvOrderFoodReadDTO, TjvOrderFoodCreateDTO, TjvOrderFoodUpdateDTO> (service, employeeService) {

    override val createLocationPath: String
        get() = "/order-food/%d"

    /** Create orders containing OrderFood on each table with type %TYPE */
    @PostMapping("type/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAtTableType (@PathVariable type: TjvTableType,
                           @RequestBody food: List<TjvOrderFoodUpdateDTO>,
                           request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.LOGGED) {
            if (food.isEmpty())
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            service.createOrderAtTypeWithFood(type, food)
        }

    @PostMapping("at/{tableId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAtTableType (@PathVariable tableId: Int,
                           @RequestBody food: List<TjvOrderFoodUpdateDTO>,
                           request: HttpServletRequest, response: HttpServletResponse)
        = authenticate(request, VisibilitySettings.LOGGED) {
            if (food.isEmpty())
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            val dto = service.createOrUpdateAtTable(tableId, food)
            response.addHeader("Location", "/order/${dto.id}")
        }

    @GetMapping("at/{tableId}")
    fun foodAtTable (@PathVariable tableId: Int, request: HttpServletRequest)
        = authenticate(request, VisibilitySettings.LOGGED) { service.getFoodAtTable(tableId) }

}