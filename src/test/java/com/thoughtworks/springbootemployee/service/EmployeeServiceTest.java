package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeCreateException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    private EmployeeService employeeService;
    private EmployeeRepository mockedEmployeeRepository;

    @BeforeEach
    void setUp() {
        mockedEmployeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeService(mockedEmployeeRepository);
    }

    @Test
    void should_return_create_employee_when_create_given_employee_service_and_employee_with_valid_age() {
        // Given
        Employee employee = new Employee("Lucy", 20, "Female", 3000, 1L);
        Employee savedEmployee = new Employee(1L, "Lucy", 20, "Female", 3000, 1L);
        when(mockedEmployeeRepository.addEmployee(employee)).thenReturn(savedEmployee);

        // When
        Employee employeeResponse = employeeService.create(employee);

        // Then
        assertEquals(savedEmployee.getId(), employeeResponse.getId());
        assertEquals("Lucy", employeeResponse.getName());
        assertEquals(20, employeeResponse.getAge());
        assertEquals("Female", employeeResponse.getGender());
        assertEquals(3000, employeeResponse.getSalary());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_whose_age_is_less_than_18() {
        // Given
        Employee employee = new Employee("Lucy", 17, "Female", 3000, 1L);

        // When, Then
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));
        assertEquals("Employee must be 18~65 years old", employeeCreateException.getMessage());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_whose_age_is_greater_than_65() {
        // Given
        Employee employee = new Employee("Lucy", 70, "Female", 3000, 1L);

        // When, Then
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));
        assertEquals("Employee must be 18~65 years old", employeeCreateException.getMessage());
    }
}
