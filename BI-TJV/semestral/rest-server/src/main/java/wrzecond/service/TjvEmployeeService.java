package wrzecond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wrzecond.employee.TjvEmployeeCreateDTO;
import wrzecond.employee.TjvEmployeeReadDTO;
import wrzecond.entity.TjvEmployee;
import wrzecond.repository.TjvEmployeeRepository;

@Service
public class TjvEmployeeService extends TjvService<TjvEmployee, TjvEmployeeReadDTO, TjvEmployeeCreateDTO> {

    // === PARAMETER ===
    private final TjvEmployeeRepository repository;

    @Autowired
    public TjvEmployeeService (TjvEmployeeRepository employeeRepository) {
        super (employeeRepository);
        this.repository = employeeRepository;
    }

    public TjvEmployee getByUsernamePassword (String username, String password) {
        return repository.getByUsernamePassword(username, password);
    }
    public void updatePassword (TjvEmployee employee, String oldPassword, String newPassword) {
        if (!oldPassword.equals(employee.getPassword()))
            throw new DataIntegrityViolationException("Old password doesn't match the old one!");

        employee.setPassword(newPassword);
        repository.save(employee);
        repository.flush();
    }

    @Override
    protected Sort getSort() {
        return Sort.by(Sort.Direction.ASC, "username");
    }

    @Override
    protected TjvEmployeeReadDTO toDTO (TjvEmployee employee) {
        return new TjvEmployeeReadDTO(employee.getId(), employee.getUsername(),
                employee.getFirstName(), employee.getLastName(), employee.getAdmin());
    }

    @Override
    protected TjvEmployee fromDTO (TjvEmployeeCreateDTO dto) {
        return new TjvEmployee(dto.getUsername(), dto.getPassword(), 
                dto.getFirstName(), dto.getLastName(), dto.isAdmin());
    }

    @Override
    protected TjvEmployee merge (TjvEmployee entity, TjvEmployeeCreateDTO dto) {
        if (dto.getUsername() != null)
            entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null)
            entity.setPassword(dto.getPassword());
        if (dto.getFirstName() != null)
            entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            entity.setLastName(dto.getLastName());
        if (dto.isAdmin() != null)
            entity.setAdmin(dto.isAdmin());

        return entity;
    }

}
