package wrzecond.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wrzecond.employee.TjvEmployeeCreateDTO;
import wrzecond.employee.TjvEmployeeReadDTO;
import wrzecond.employee.TjvPasswordDTO;
import wrzecond.entity.TjvEmployee;
import wrzecond.service.TjvEmployeeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@TjvController.Visibility (
    findAll = TjvController.VisibilitySettings.ADMIN,
    getByID = TjvController.VisibilitySettings.ADMIN
)
@RequestMapping("/employees")
public class TjvEmployeeController extends TjvController<TjvEmployee, TjvEmployeeReadDTO, TjvEmployeeCreateDTO> {

    // === PROPERTIES ===
    private final TjvEmployeeService service;

    @Autowired
    public TjvEmployeeController(TjvEmployeeService employeeService) {
        super(employeeService, employeeService);
        this.service = employeeService;
    }

    @PatchMapping("/passChange")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void passChange (@RequestBody TjvPasswordDTO dto, HttpServletRequest request) {
        TjvEmployee employee = authenticate(request, VisibilitySettings.LOGGED);
        try {
            service.updatePassword(employee, dto.getOldPassword(), dto.getNewPassword());
        }
        catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    TjvEmployeeReadDTO login (HttpServletRequest request) {
        TjvEmployee employee = authenticate(request, VisibilitySettings.LOGGED);
        return service.getByID(employee.getId());
    }

    @Override
    protected String getCreateLocationPath() {
        return "/employees/%d";
    }

}
