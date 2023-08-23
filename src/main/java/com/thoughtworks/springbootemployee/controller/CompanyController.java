package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.CompanyService;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> listAll() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public Company findById(@PathVariable Long id) {
        return companyService.findById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployees(@PathVariable Long id) {
        return companyService.findEmployeesByCompanyId(id);
    }

    @GetMapping(params = {"pageNumber", "pageSize"})
    public List<Company> listByPage(@RequestParam Long pageNumber, @RequestParam Long pageSize) {
        return companyService.getCompaniesByPage(pageNumber, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company newCompanyInfo) {
        return companyService.update(id, newCompanyInfo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
    }
}
