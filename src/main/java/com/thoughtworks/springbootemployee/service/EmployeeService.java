package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeCreateException;
import com.thoughtworks.springbootemployee.exception.EmployeeUpdateException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
        if (matchedEmployee.isInactive()) {
            throw new EmployeeUpdateException();
        }
        matchedEmployee.setAge(newEmployeeInfo.getAge());
        matchedEmployee.setSalary(newEmployeeInfo.getSalary());
        return employeeRepository.updateEmployee(id, matchedEmployee);
    }
}
