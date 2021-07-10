package wrzecond.controller;

import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.food.TjvFoodCreateDTO;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.entity.TjvFood;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvFoodService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@TjvController.Visibility
@RequestMapping("/food/")
public class TjvFoodController extends TjvController<TjvFood, TjvFoodReadDTO, TjvFoodCreateDTO> {

    // Properties
    private final TjvFoodService service;

    public TjvFoodController (TjvFoodService foodService, TjvEmployeeService authService) {
        super (foodService, authService);
        this.service = foodService;
    }

    @GetMapping("allergens/{id}")
    List<TjvAllergenReadDTO> getAllergensInFood (@PathVariable int id, HttpServletRequest request) {
        authenticate(request, VisibilitySettings.ALL);
        try {
            return service.getAllergensInFood(id);
        }
        catch (EntityNotFoundException | JpaObjectRetrievalFailureException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    protected String getCreateLocationPath () {
        return "/food/%d";
    }

}
