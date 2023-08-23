package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {

    private CompanyService companyService;
    private CompanyRepository mockedCompanyRepository;
    private EmployeeRepository mockedEmployeeRepository;

    @BeforeEach
    void setUp() {
        mockedCompanyRepository = mock(CompanyRepository.class);
        mockedEmployeeRepository = mock(EmployeeRepository.class);
        companyService = new CompanyService(mockedCompanyRepository);
    }

    @Test
    void should_return_all_companies_when_get_companies_given_companies_service() {
        // Given
        Company company = new Company("OOCL");
        List<Company> companies = List.of(company);
        when(mockedCompanyRepository.getAllCompanies()).thenReturn(companies);

        // When
        List<Company> allCompanies = companyService.getAll();

        // Then
        assertEquals(allCompanies.get(0).getId(), company.getId());
        assertEquals(allCompanies.get(0).getName(), company.getName());
    }
}
