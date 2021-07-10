package wrzecond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wrzecond.allergen.TjvAllergenCreateDTO;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.entity.TjvAllergen;
import wrzecond.service.TjvAllergenService;
import wrzecond.service.TjvEmployeeService;

@RestController
@TjvController.Visibility (
    getByID = TjvController.VisibilitySettings.NONE
)
@RequestMapping("/allergens")
public class TjvAllergenController extends TjvController<TjvAllergen, TjvAllergenReadDTO, TjvAllergenCreateDTO> {

    @Autowired
    public TjvAllergenController (TjvAllergenService allergenService, TjvEmployeeService authService) {
        super (allergenService, authService);
    }

    @Override
    protected String getCreateLocationPath () {
        return "/allergens/%d";
    }

}
