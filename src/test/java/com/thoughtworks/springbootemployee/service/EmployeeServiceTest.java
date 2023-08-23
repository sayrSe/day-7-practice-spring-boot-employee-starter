package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeCreateException;
import com.thoughtworks.springbootemployee.exception.EmployeeUpdateException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void should_return_all_employees_when_get_employees_given_employee_service() {
        // Given
        Employee employee = new Employee(1L, "Lucy", 20, "Female", 3000);
        List<Employee> employees = List.of(employee);
        when(mockedEmployeeRepository.getAllEmployees()).thenReturn(employees);

        // When
        List<Employee> allEmployees = employeeService.getAll();

        // Then
        assertEquals(allEmployees.get(0).getId(), employee.getId());
        assertEquals(allEmployees.get(0).getName(), employee.getName());
        assertEquals(allEmployees.get(0).getAge(), employee.getAge());
        assertEquals(allEmployees.get(0).getGender(), employee.getGender());
        assertEquals(allEmployees.get(0).getSalary(), employee.getSalary());
    }

    @Test
    void should_return_the_employee_when_get_employee_given_employee_service_and_an_employee_id() {
    	// Given
        Employee employee = new Employee(null, "Lucy", 20, "Female", 3000);
        when(mockedEmployeeRepository.findEmployeeById(employee.getId())).thenReturn(employee);

    	// When
        Employee foundEmployee = employeeService.findById(employee.getId());

    	// Then
        assertEquals(employee.getId(), foundEmployee.getId());
        assertEquals(employee.getName(), foundEmployee.getName());
        assertEquals(employee.getAge(), foundEmployee.getAge());
        assertEquals(employee.getGender(), foundEmployee.getGender());
        assertEquals(employee.getSalary(), foundEmployee.getSalary());
    }

    @Test
    void should_return_employees_by_given_gender_when_get_employees_given_employee_service() {
        // Given
        Employee alice = new Employee(null, "Alice", 24, "Female", 9000);
        Employee bob = new Employee(null, "Bob", 28, "Male", 8000);
        List<Employee> employees = List.of(alice, bob);
        when(mockedEmployeeRepository.findEmployeeByGender(anyString())).thenReturn(employees);

        // When
        List<Employee> foundEmployees = employeeService.findByGender("Female");

        // Then
        assertEquals(foundEmployees.get(0).getId(), alice.getId());
        assertEquals(foundEmployees.get(0).getName(), alice.getName());
        assertEquals(foundEmployees.get(0).getAge(), alice.getAge());
        assertEquals(foundEmployees.get(0).getGender(), alice.getGender());
        assertEquals(foundEmployees.get(0).getSalary(), alice.getSalary());
    }

    @Test
    void should_return_created_active_employee_when_create_given_employee_service_and_employee_with_valid_age() {
        // Given
        Employee employee = new Employee(null, "Lucy", 20, "Female", 3000);
        Employee savedEmployee = new Employee(1L, "Lucy", 20, "Female", 3000);
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
        Employee employee = new Employee(null, "Lucy", 17, "Female", 3000);

        // When, Then
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));
        assertEquals("Employee must be 18~65 years old", employeeCreateException.getMessage());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_whose_age_is_greater_than_65() {
        // Given
        Employee employee = new Employee(null, "Lucy", 70, "Female", 3000);

        // When, Then
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));
        assertEquals("Employee must be 18~65 years old", employeeCreateException.getMessage());
    }

    @Test
    void should_return_inactive_employee_when_delete_given_employee_service_and_active_employee() {
        // Given
        Employee employee = new Employee(null, "Lucy", 20, "Female", 3000);
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
        Employee employee = new Employee(null, "Lucy", 20, "Female", 3000);
        employee.setActive(Boolean.TRUE);
        Employee updatedEmployeeInfo = new Employee(null, null, 30, null, 10000);
        when(mockedEmployeeRepository.findEmployeeById(employee.getId())).thenReturn(employee);
        when(mockedEmployeeRepository.updateEmployee(employee.getId(), updatedEmployeeInfo)).thenReturn(employee);

        // When
        Employee updatedEmployee = employeeService.update(employee.getId(), updatedEmployeeInfo);

        // Then
        assertEquals("Lucy", updatedEmployee.getName());
        assertEquals("Female", updatedEmployee.getGender());
        verify(mockedEmployeeRepository).updateEmployee(eq(employee.getId()), argThat(tempEmployee -> {
            assertEquals(30, tempEmployee.getAge());
            assertEquals(10000, tempEmployee.getSalary());
            return true;
        }));
    }

    @Test
    void should_throw_exception_when_update_given_employee_service_and_inactive_employee_and_age_and_salary() {
        // Given
        Employee employee = new Employee(null, "Lucy", 20, "Female", 3000);
        employee.setActive(Boolean.FALSE);
        Employee updatedEmployeeInfo = new Employee(null, "Lucy", 30, "Female", 10000);
        when(mockedEmployeeRepository.findEmployeeById(employee.getId())).thenReturn(employee);

        // When, Then
        EmployeeUpdateException employeeUpdateException = assertThrows(EmployeeUpdateException.class, () ->
                employeeService.update(null, updatedEmployeeInfo));
        assertEquals("Employee is inactive", employeeUpdateException.getMessage());
    }
}
