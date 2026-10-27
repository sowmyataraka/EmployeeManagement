package com.a2z.employeemanagement.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    List<Employee> findByDepartmentIgnoreCaseOrderByLastNameAsc(String department);
}
