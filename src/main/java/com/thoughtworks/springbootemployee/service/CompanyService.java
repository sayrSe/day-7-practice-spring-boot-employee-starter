package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;

import java.util.List;

public class CompanyService {

    private final CompanyRepository companyRepository;
    private  final EmployeeRepository employeeRepository;

    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Company> getAll() {
        return companyRepository.getAllCompanies();
    }

    public Company findById(Long id) {
        return companyRepository.findCompanyById(id);
    }

    public Company create(Company company) {
        return companyRepository.addCompany(company);
    }

    public List<Employee> findEmployeesByCompanyId(Long id) {
        return employeeRepository.findEmployeesByCompanyId(id);
    }

    public void delete(Long id) {
        Company matchedCompany = companyRepository.findCompanyById(id);
        matchedCompany.setActive(Boolean.FALSE);
        companyRepository.updateCompany(id, matchedCompany);
    }

    public Company update(Long id, Company updatedCompanyInfo) {
        return companyRepository.updateCompany(id, updatedCompanyInfo);
    }

    public List<Company> getCompaniesByPage(Long pageNumber, Long pageSize) {
        return companyRepository.listCompaniesByPage(pageNumber, pageSize);
    }
}