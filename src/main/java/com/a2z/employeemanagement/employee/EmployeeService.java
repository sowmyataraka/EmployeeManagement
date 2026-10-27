package com.a2z.employeemanagement.employee;

import com.a2z.employeemanagement.shared.ConflictException;
import com.a2z.employeemanagement.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException("An employee with this email already exists");
        }
        Employee employee = new Employee(
                request.firstName().trim(), request.lastName().trim(), request.email().trim().toLowerCase(),
                request.department().trim(), request.jobTitle().trim(),
                request.salary(), request.joiningDate()
        );
        return EmployeeResponse.from(repository.save(employee));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll(String department) {
        List<Employee> employees = (department == null || department.isBlank())
                ? repository.findAll()
                : repository.findByDepartmentIgnoreCaseOrderByLastNameAsc(department.trim());
        return employees.stream().map(EmployeeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return EmployeeResponse.from(getEmployee(id));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEmployee(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new ConflictException("Another employee already uses this email");
        }
        employee.update(
                request.firstName().trim(), request.lastName().trim(), request.email().trim().toLowerCase(),
                request.department().trim(), request.jobTitle().trim(),
                request.salary(), request.joiningDate()
        );
        return EmployeeResponse.from(repository.save(employee));
    }

    public void delete(Long id) {
        repository.delete(getEmployee(id));
    }

    private Employee getEmployee(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }
}
