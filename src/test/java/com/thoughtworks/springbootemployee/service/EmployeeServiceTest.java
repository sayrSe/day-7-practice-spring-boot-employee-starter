package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeCreateException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService;
    private EmployeeRepository mockedEmployeeRepository;

    @BeforeEach
    void setUp() {
        mockedEmployeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeService(mockedEmployeeRepository);
    }

    @Test
    void should_return_created_active_employee_when_create_given_employee_service_and_employee_with_valid_age() {
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
        verify(mockedEmployeeRepository).addEmployee(argThat(tempEmployee -> {
            assertTrue(tempEmployee.isActive());
            return true;
        }));
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

    @Test
    void should_return_inactive_employee_when_delete_given_employee_service_and_active_employee() {
        // Given
        Employee employee = new Employee("Lucy", 20, "Female", 3000, 1L);
        employee.setActive(Boolean.TRUE);
        when(mockedEmployeeRepository.findEmployeeById(employee.getId())).thenReturn(employee);

        // When
        employeeService.delete(employee.getId());

        // Then
        verify(mockedEmployeeRepository).updateEmployee(eq(employee.getId()), argThat(tempEmployee -> {
            assertFalse(tempEmployee.isActive());
            assertEquals("Lucy", tempEmployee.getName());
            assertEquals(20, tempEmployee.getAge());
            assertEquals("Female", tempEmployee.getGender());
            assertEquals(3000, tempEmployee.getSalary());
            return true;
        }));
    }

    @Test
    void should_return_updated_employee_when_update_given_employee_age_and_salary() {
        // Given
        Employee employee = new Employee("Lucy", 20, "Female", 3000, 1L);
        employee.setActive(Boolean.TRUE);
        Employee updatedEmployeeInfo = new Employee();
        updatedEmployeeInfo.setAge(30);
        updatedEmployeeInfo.setSalary(10000);
        when(mockedEmployeeRepository.findEmployeeById(employee.getId())).thenReturn(employee);
        when(mockedEmployeeRepository.updateEmployee(employee.getId(), employee)).thenReturn(employee);

        // When
        Employee updatedEmployee = employeeService.update(employee.getId(), updatedEmployeeInfo);

        // Then
        assertEquals("Lucy", updatedEmployee.getName());
        assertEquals(30, updatedEmployee.getAge());
        assertEquals("Female", updatedEmployee.getGender());
        assertEquals(10000, updatedEmployee.getSalary());
    }
}
