package com.thoughtworks.springbootemployee.controller;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("Employee not found");
    }
}
