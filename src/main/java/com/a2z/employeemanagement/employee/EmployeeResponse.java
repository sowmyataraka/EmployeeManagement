package com.a2z.employeemanagement.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id, String firstName, String lastName, String email,
        String department, String jobTitle, BigDecimal salary, LocalDate joiningDate
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(), employee.getFirstName(), employee.getLastName(),
                employee.getEmail(), employee.getDepartment(), employee.getJobTitle(),
                employee.getSalary(), employee.getJoiningDate()
        );
    }
}
