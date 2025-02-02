package com.thoughtworks.springbootemployee.repository;

import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {

    private static final List<Employee> employees = new ArrayList<>();
    public static final long EMPTY_LIST_SIZE = 0L;
    public static final int ID_INCREMENT = 1;

    static {
        employees.add(new Employee(1L, "Alice", 30, "Female", 5000, 1L));
        employees.add(new Employee(2L, "Bob", 31, "Male", 5000, 2L));
        employees.add(new Employee(3L, "Carl", 32, "Male", 5000, 1L));
        employees.add(new Employee(4L, "David", 33, "Male", 5000, 2L));
        employees.add(new Employee(5L, "Ellen", 34, "Female", 5000, 3L));
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee findEmployeeById(Long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> findEmployeeByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public Employee addEmployee(Employee employee) {
        Long id = generateNextEmployeeId();
        Employee newEmployee = new Employee(
                id,
                employee.getName(),
                employee.getAge(),
                employee.getGender(),
                employee.getSalary(),
                employee.getCompanyId());

        employees.add(newEmployee);
        return newEmployee;
    }

    private Long generateNextEmployeeId() {
        return employees.stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(EMPTY_LIST_SIZE) + ID_INCREMENT;
    }

    public List<Employee> listEmployeesByPage(Long pageNumber, Long pageSize) {
        return employees.stream()
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Employee updateEmployee(Long id, Employee newEmployeeInfo) {
        Employee employee = findEmployeeById(id);
        employee.setAge(newEmployeeInfo.getAge());
        employee.setSalary(newEmployeeInfo.getSalary());
        return employee;
    }

    public List<Employee> findEmployeesByCompanyId(Long id) {
        return employees.stream()
                .filter(employee -> employee.getCompanyId().equals(id))
                .collect(Collectors.toList());
    }

    public void cleanAll() {
        employees.clear();
    }
}
