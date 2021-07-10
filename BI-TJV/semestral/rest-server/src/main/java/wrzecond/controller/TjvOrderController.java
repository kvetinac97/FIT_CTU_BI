package wrzecond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.TjvOrder;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@TjvController.Visibility (
    create = TjvController.VisibilitySettings.LOGGED,
    findAll = TjvController.VisibilitySettings.LOGGED,
    update = TjvController.VisibilitySettings.LOGGED,
    delete = TjvController.VisibilitySettings.LOGGED,
    getByID = TjvController.VisibilitySettings.NONE
)
@RequestMapping("/orders/")
public class TjvOrderController extends TjvController<TjvOrder, TjvOrderReadDTO, TjvOrderCreateDTO> {

    private final TjvOrderService orderService;

    @Autowired
    public TjvOrderController (TjvOrderService orderService, TjvEmployeeService authService) {
        super (orderService, authService);
        this.orderService = orderService;
    }

    @GetMapping("at/{id}")
    List<TjvOrderReadDTO> findByTableID (@PathVariable int id, HttpServletRequest request) {
        authenticate(request, VisibilitySettings.LOGGED);
        return orderService.findByTableID(id);
    }

    @Override
    protected String getCreateLocationPath () {
        return "/orders/%d";
    }

}
