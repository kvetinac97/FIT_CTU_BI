package cz.cvut.fit.caltrack.presentation.controller

import cz.cvut.fit.caltrack.business.dto.FoodCreateDTO
import cz.cvut.fit.caltrack.business.dto.FoodReadDTO
import cz.cvut.fit.caltrack.business.dto.FoodUpdateDTO
import cz.cvut.fit.caltrack.data.entity.Food
import cz.cvut.fit.caltrack.business.service.FoodService
import cz.cvut.fit.caltrack.business.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/food")
class FoodController (override val service: FoodService, userService: UserService)
: IControllerImpl<Food, FoodReadDTO, FoodCreateDTO, FoodUpdateDTO>(service, userService) {

    /**
     * Search for food matching given name
     * @param name name of food being searched for
     * @param request current HTTP request, used for authentication
     * @return list of food matching given name
     */
    @GetMapping("/search/{name}")
    fun findByName (@PathVariable name: String, request: HttpServletRequest) : List<FoodReadDTO>
        = authenticate(request) { service.searchByName(name) }

}