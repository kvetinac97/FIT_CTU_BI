package wrzecond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvOrderService;
import wrzecond.table.TjvTableCreateDTO;
import wrzecond.table.TjvTableReadDTO;
import wrzecond.entity.TjvTable;
import wrzecond.service.TjvTableService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@TjvController.Visibility (
    findAll = TjvController.VisibilitySettings.LOGGED,
    getByID = TjvController.VisibilitySettings.NONE
)
@RequestMapping("/tables")
public class TjvTableController extends TjvController<TjvTable, TjvTableReadDTO, TjvTableCreateDTO> {

    // Help method
    private final TjvOrderService orderService;

    @Autowired
    public TjvTableController(TjvTableService tableService, TjvOrderService orderService,
                              TjvEmployeeService authService) {
        super(tableService, authService);
        this.orderService = orderService;
    }

    @GetMapping
    @Override
    List<TjvTableReadDTO> all (HttpServletRequest request) {
        List<TjvTableReadDTO> dtoList = super.all(request);
        return dtoList
            .stream()
            .map(dto -> dto.setEmpty(orderService.findByTableID(dto.getId()).isEmpty()))
            .collect(Collectors.toList());
    }

    @Override
    protected String getCreateLocationPath() {
        return "/tables/%d";
    }

}
