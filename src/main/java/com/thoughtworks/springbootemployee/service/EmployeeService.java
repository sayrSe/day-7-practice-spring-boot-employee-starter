package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeCreateException;
import com.thoughtworks.springbootemployee.exception.EmployeeUpdateException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.getAllEmployees();
    }

    public Employee findById(Long id) {
        return employeeRepository.findEmployeeById(id);
    }

    public List<Employee> findByGender(String gender) {
        return employeeRepository.findEmployeeByGender(gender);
    }

    public Employee create(Employee employee) {
        if (employee.hasInvalidAge()) {
            throw new EmployeeCreateException();
        }
        employee.setActive(Boolean.TRUE);
        return employeeRepository.addEmployee(employee);
    }

    public void delete(Long id) {
        Employee matchedEmployee = employeeRepository.findEmployeeById(id);
        matchedEmployee.setActive(Boolean.FALSE);
        employeeRepository.updateEmployee(id, matchedEmployee);
    }

    public Employee update(Long id, Employee newEmployeeInfo) {
        Employee matchedEmployee = employeeRepository.findEmployeeById(id);
        if (Boolean.TRUE.equals(matchedEmployee.isInactive())) {
            throw new EmployeeUpdateException();
        }
        return employeeRepository.updateEmployee(id, newEmployeeInfo);
    }

    public List<Employee> getEmployeesByPage(Long pageNumber, Long pageSize) {
        return employeeRepository.listEmployeesByPage(pageNumber, pageSize);
    }
}
