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
        employees.add(new Employee(1L, "Alice", 30, "Female", 5000));
        employees.add(new Employee(2L, "Bob", 31, "Male", 5000));
        employees.add(new Employee(3L, "Carl", 32, "Male", 5000));
        employees.add(new Employee(4L, "David", 33, "Male", 5000));
        employees.add(new Employee(5L, "Ellen", 34, "Female", 5000));
    }

    public List<Employee> listAll() {
        return employees;
    }

    public Employee findById(Long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public void addEmployee(Employee employee) {
        Long id = generateNextId();

        Employee newEmployee = new Employee(id, employee.getName(), employee.getAge(), employee.getGender(), employee.getSalary());
        employees.add(newEmployee);
    }

    private Long generateNextId() {
        return employees.stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(EMPTY_LIST_SIZE) + ID_INCREMENT;
    }

    public List<Employee> listByPage(Long pageNumber, Long pageSize) {
        return employees.stream()
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Employee removeEmployee(Employee employee) {
        employees.remove(employee);
        return employee;
    }
}
