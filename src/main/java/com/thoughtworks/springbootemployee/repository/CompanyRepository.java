package com.thoughtworks.springbootemployee.repository;

import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepository {

    private static final List<Company> companies = new ArrayList<>();
    public static final long EMPTY_LIST_SIZE = 0L;
    public static final int ID_INCREMENT = 1;

    static {
        companies.add(new Company(1L, "Orient Overseas Container Line"));
        companies.add(new Company(2L, "COSCO Shipping Lines"));
        companies.add(new Company(3L, "Thoughtworks"));
        companies.add(new Company(4L, "Microsoft"));
        companies.add(new Company(5L, "Apple"));
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public Company findCompanyById(Long id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(CompanyNotFoundException::new);
    }

    public List<Company> listCompaniesByPage(Long pageNumber, Long pageSize) {
        return companies.stream()
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Company addCompany(Company company) {
        Long id = generateNextCompanyId();
        Company newCompany = new Company(id, company.getName());
        companies.add(newCompany);
        return newCompany;
    }

    public Company updateCompany(Long id, Company newCompanyInfo) {
        Company company = findCompanyById(id);
        company.setName(newCompanyInfo.getName());
        return company;
    }

    private Long generateNextCompanyId() {
        return companies.stream()
                .mapToLong(Company::getId)
                .max()
                .orElse(EMPTY_LIST_SIZE) + ID_INCREMENT;
    }

    public void deleteCompany(Long id) {
        Company company = findCompanyById(id);
        companies.remove(company);
    }
}
