package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Company> listAll() {
        return companyRepository.listAll();
    }

    @GetMapping("/{id}")
    public Company findById(@PathVariable Long id) {
        return companyRepository.findById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployees(@PathVariable Long id) {
        return employeeRepository.findEmployeesByCompanyId(id);
    }

    @GetMapping(params = {"pageNumber", "pageSize"})
    public List<Company> listByPage(@RequestParam Long pageNumber, @RequestParam Long pageSize) {
        return companyRepository.listByPage(pageNumber, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        return companyRepository.addCompany(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company newCompanyInfo) {
        Company company = companyRepository.findById(id);
        company.setName(newCompanyInfo.getName());
        return company;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(id);
        companyRepository.deleteCompany(company);
    }
}
