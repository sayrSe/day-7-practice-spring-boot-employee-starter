package com.thoughtworks.springbootemployee.exception;

public class EmployeeUpdateException extends RuntimeException {

    public EmployeeUpdateException() {
        super("Employee is inactive");
    }
}
