package com.thoughtworks.springbootemployee.repository;

import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {

    private static final List<Company> companies = new ArrayList<>();

    static {
        companies.add(new Company(1L, "Orient Overseas Container Line"));
        companies.add(new Company(2L, "Cosco Shipping Lines"));
        companies.add(new Company(3L, "Thoughtworks"));
    }

    public List<Company> listAll() {
        return companies;
    }
}
