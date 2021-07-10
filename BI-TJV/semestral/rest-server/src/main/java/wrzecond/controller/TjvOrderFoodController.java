package wrzecond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wrzecond.order_food.TjvOrderFoodCreateDTO;
import wrzecond.order_food.TjvOrderFoodReadDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.TjvOrderFood;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvOrderFoodService;
import wrzecond.table.TjvTableType;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@TjvController.Visibility (
    create = TjvController.VisibilitySettings.LOGGED,
    delete = TjvController.VisibilitySettings.LOGGED,
    update = TjvController.VisibilitySettings.LOGGED,
    findAll = TjvController.VisibilitySettings.NONE,
    getByID = TjvController.VisibilitySettings.NONE
)
@RequestMapping("/order-food/")
public class TjvOrderFoodController extends TjvController<TjvOrderFood, TjvOrderFoodReadDTO, TjvOrderFoodCreateDTO> {

    private final TjvOrderFoodService service;

    @Autowired
    public TjvOrderFoodController (TjvOrderFoodService orderFoodService, TjvEmployeeService authService) {
        super (orderFoodService, authService);
        this.service = orderFoodService;
    }

    /** Create orders containing OrderFood on each table with type %TYPE */
    @PostMapping("type/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    List<TjvOrderReadDTO> createAtTableType (@PathVariable TjvTableType type,
                                             @RequestBody List<TjvOrderFoodCreateDTO> food,
                                             HttpServletRequest request) {
        if ( food.isEmpty() )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        authenticate(request, VisibilitySettings.LOGGED);
        try {
            return service.createOrderAtTypeWithFood(type, food);
        }
        catch (DataIntegrityViolationException x) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException x) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("at/{tableId}")
    @ResponseStatus(HttpStatus.CREATED)
    void createOrUpdateAtTable (@PathVariable int tableId,
                                   @RequestBody List<TjvOrderFoodCreateDTO> food,
                                   HttpServletRequest request, HttpServletResponse response) {
        if ( food.isEmpty() )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        authenticate(request, VisibilitySettings.LOGGED);
        try {
            TjvOrderReadDTO dto = service.createOrUpdateAtTable(tableId, food);
            response.addHeader("Location", "/order/" + dto.getId());
        }
        catch (DataIntegrityViolationException x) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException x) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("at/{tableId}")
    List<TjvOrderFoodReadDTO> foodAtTable (@PathVariable int tableId, HttpServletRequest request) {
        authenticate(request, VisibilitySettings.LOGGED);
        try {
            return service.getFoodAtTable(tableId);
        }
        catch (EntityNotFoundException x) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    protected String getCreateLocationPath () {
        return "/order-food/%d";
    }

}
