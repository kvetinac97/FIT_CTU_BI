package wrzecond.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import wrzecond.TjvEmployeeCreateDTO
import wrzecond.TjvEmployeeReadDTO
import wrzecond.TjvEmployeeUpdateDTO
import wrzecond.entity.TjvEmployee
import wrzecond.repository.TjvEmployeeRepository

@Service
class TjvEmployeeService (override val repository: TjvEmployeeRepository)
: TjvService<TjvEmployee, TjvEmployeeReadDTO, TjvEmployeeCreateDTO, TjvEmployeeUpdateDTO>(repository) {

    fun getByUsernamePassword (username: String, password: String)
        = tryCatch { repository.getByUsernamePassword(username, password) }

    fun updatePassword (employee: TjvEmployee, oldPassword: String, newPassword: String) {
        if (oldPassword != employee.password)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST) // same password

        tryCatch {
            repository.save(employee.merge(TjvEmployeeUpdateDTO(password = newPassword)))
            repository.flush()
        }
    }

    override fun TjvEmployee.toDTO() = TjvEmployeeReadDTO(id, username, firstName, lastName, admin)
    override fun TjvEmployeeCreateDTO.toEntity() = TjvEmployee(username, password, firstName, lastName, admin)
    override fun TjvEmployee.merge (dto: TjvEmployeeUpdateDTO) = TjvEmployee(
        dto.username ?: username,
        dto.password ?: password,
        dto.firstName ?: firstName,
        dto.lastName ?: lastName,
        dto.admin ?: admin,
        id
    )

}