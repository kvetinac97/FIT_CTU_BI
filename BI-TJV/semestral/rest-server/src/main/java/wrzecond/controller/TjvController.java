package wrzecond.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wrzecond.ICreateDTO;
import wrzecond.IReadDTO;
import wrzecond.entity.TjvEmployee;
import wrzecond.entity.TjvEntity;
import wrzecond.service.TjvEmployeeService;
import wrzecond.service.TjvService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.*;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
abstract class TjvController<T extends TjvEntity, R extends IReadDTO, C extends ICreateDTO> {

    // === PARAMETERS ===
    private final TjvService<T, R, C> service;
    private final TjvEmployeeService employeeService;
    protected abstract String getCreateLocationPath ();

    // === AUTHENTICATION ===
    private void authenticate (HttpServletRequest request, Function<Visibility, VisibilitySettings> function) {
        Visibility annotation = getClass().getAnnotation(Visibility.class);
        authenticate(request, function.apply(annotation));
    }
    protected TjvEmployee authenticate (HttpServletRequest request, VisibilitySettings settings) {
        // Init properties from request
        String username = request.getHeader("Username");
        String password = request.getHeader("Password");
        TjvEmployee employee = employeeService.getByUsernamePassword(username, password);

        // See authorization status
        switch (settings) {
            case ALL:
                break;
            case LOGGED:
                // Not logged in
                if (employee == null)
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                break;
            case ADMIN:
                // Not logged in
                if (employee == null)
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                // Not sufficient permissions
                if (!employee.getAdmin())
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                break;
            default:
            case NONE:
                throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }

        return employee;
    }

    @GetMapping
    List<R> all (HttpServletRequest request) {
        authenticate(request, Visibility::findAll);
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void create (@RequestBody C table, HttpServletRequest request, HttpServletResponse response) {
        authenticate(request, Visibility::create);
        try {
            R dto = service.create(table);
            response.addHeader("Location", String.format(getCreateLocationPath(), dto.getId()));
        }
        catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update (@PathVariable int id, @RequestBody C table, HttpServletRequest request) {
        authenticate(request, Visibility::update);
        try {
            service.update(id, table);
        }
        catch (EntityNotFoundException | JpaObjectRetrievalFailureException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    R getByID (@PathVariable int id, HttpServletRequest request) {
        authenticate(request, Visibility::getByID);
        try {
            return service.getByID(id);
        }
        catch ( EntityNotFoundException exc ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete (@PathVariable int id, HttpServletRequest request) {
        authenticate(request, Visibility::delete);
        try {
            service.delete(id);
        }
        catch ( EmptyResultDataAccessException exception ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    protected @interface Visibility {
        VisibilitySettings findAll() default VisibilitySettings.ALL;
        VisibilitySettings getByID() default VisibilitySettings.ALL;
        
        VisibilitySettings create() default VisibilitySettings.ADMIN;
        VisibilitySettings delete() default VisibilitySettings.ADMIN;
        VisibilitySettings update() default VisibilitySettings.ADMIN;
    }
    protected enum VisibilitySettings {
        NONE,
        ADMIN,
        LOGGED,
        ALL
    }

}
