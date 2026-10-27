package com.a2z.employeemanagement.employee;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email @Size(max = 180) String email,
        @NotBlank @Size(max = 100) String department,
        @NotBlank @Size(max = 120) String jobTitle,
        @NotNull @DecimalMin(value = "0.00", inclusive = false) @Digits(integer = 10, fraction = 2)
        BigDecimal salary,
        @NotNull @PastOrPresent LocalDate joiningDate
) { }
