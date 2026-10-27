package com.a2z.employeemanagement.employee;

import com.a2z.employeemanagement.shared.ConflictException;
import com.a2z.employeemanagement.shared.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest(
                " Ada ", " Lovelace ", "ADA@example.com", "Engineering",
                "Software Engineer", new BigDecimal("85000.00"), LocalDate.of(2020, 1, 10)
        );
    }

    @Test
    void createSavesEmployeeAndNormalizesTextAndEmail() {
        when(repository.existsByEmailIgnoreCase("ADA@example.com")).thenReturn(false);
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee saved = invocation.getArgument(0);
            return saved;
        });

        EmployeeResponse response = service.create(request);

        assertEquals("Ada", response.firstName());
        assertEquals("Lovelace", response.lastName());
        assertEquals("ada@example.com", response.email());
        verify(repository).save(any(Employee.class));
    }

    @Test
    void createRejectsDuplicateEmail() {
        when(repository.existsByEmailIgnoreCase(request.email())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdReturnsEmployeeWhenPresent() {
        Employee employee = new Employee("Ada", "Lovelace", "ada@example.com", "Engineering",
                "Engineer", new BigDecimal("85000.00"), LocalDate.of(2020, 1, 10));
        when(repository.findById(7L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = service.findById(7L);

        assertEquals("ada@example.com", response.email());
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void updateRejectsEmailUsedByAnotherEmployee() {
        Employee employee = new Employee("Ada", "Lovelace", "ada@example.com", "Engineering",
                "Engineer", new BigDecimal("85000.00"), LocalDate.of(2020, 1, 10));
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.existsByEmailIgnoreCaseAndIdNot(request.email(), 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.update(1L, request));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteRemovesExistingEmployee() {
        Employee employee = new Employee("Ada", "Lovelace", "ada@example.com", "Engineering",
                "Engineer", new BigDecimal("85000.00"), LocalDate.of(2020, 1, 10));
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.delete(1L);

        verify(repository).delete(employee);
    }
}
