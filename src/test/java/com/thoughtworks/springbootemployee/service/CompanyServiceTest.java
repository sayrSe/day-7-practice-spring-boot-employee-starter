package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {

    private CompanyService companyService;
    private CompanyRepository mockedCompanyRepository;
    private EmployeeRepository mockedEmployeeRepository;

    @BeforeEach
    void setUp() {
        mockedCompanyRepository = mock(CompanyRepository.class);
        mockedEmployeeRepository = mock(EmployeeRepository.class);
        companyService = new CompanyService(mockedCompanyRepository, mockedEmployeeRepository);
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

    @Test
    void should_return_the_company_when_get_company_given_company_service_and_an_company_id() {
        // Given
        Company company = new Company("OOCL");
        when(mockedCompanyRepository.findCompanyById(company.getId())).thenReturn(company);

        // When
        Company foundCompany = companyService.findById(company.getId());

        // Then
        assertEquals(company.getId(), foundCompany.getId());
        assertEquals(company.getName(), foundCompany.getName());
    }

    @Test
    void should_return_employees_by_given_company_when_get_find_employees_by_company_id_given_company_service() {
        // Given
        Company company = new Company(1L, "OOCL");
        Employee alice = new Employee(null, "Alice", 24, "Female", 9000, company.getId());

        List<Employee> employees = List.of(alice);
        when(mockedEmployeeRepository.findEmployeesByCompanyId(company.getId())).thenReturn(employees);

        // When
        List<Employee> foundEmployees = companyService.findEmployeesByCompanyId(company.getId());

        // Then
        assertEquals(foundEmployees.get(0).getId(), alice.getId());
        assertEquals(foundEmployees.get(0).getName(), alice.getName());
        assertEquals(foundEmployees.get(0).getAge(), alice.getAge());
        assertEquals(foundEmployees.get(0).getGender(), alice.getGender());
        assertEquals(foundEmployees.get(0).getSalary(), alice.getSalary());
    }

    @Test
    void should_return_inactive_company_when_delete_given_company_service_and_active_company() {
        // Given
        Company company = new Company("OOCL");
        company.setActive(Boolean.TRUE);
        when(mockedCompanyRepository.findCompanyById(company.getId())).thenReturn(company);

        // When
        companyService.delete(company.getId());

        // Then
        verify(mockedCompanyRepository).updateCompany(eq(company.getId()), argThat(tempCompany -> {
            assertFalse(tempCompany.isActive());
            assertEquals("OOCL", tempCompany.getName());
            return true;
        }));
    }

    @Test
    void should_return_created_company_when_create_given_company_service_and_company() {
        // Given
        Company company = new Company("OOCL");
        Company savedCompany = new Company(1L, "OOCL");
        when(mockedCompanyRepository.addCompany(company)).thenReturn(savedCompany);

        // When
        Company companyResponse = companyService.create(company);

        // Then
        assertEquals(savedCompany.getId(), companyResponse.getId());
        assertEquals("OOCL", companyResponse.getName());
    }

    @Test
    void should_return_updated_company_when_update_given_company_name() {
        // Given
        Company company = new Company(null, "OOCL");
        Company updatedCompanyInfo = new Company(null, "Thoughtworks");
        when(mockedCompanyRepository.updateCompany(company.getId(), updatedCompanyInfo)).thenReturn(updatedCompanyInfo);

        // When
        Company updatedCompany = companyService.update(company.getId(), updatedCompanyInfo);

        // Then
        assertEquals(updatedCompanyInfo.getId(), updatedCompany.getId());
        assertEquals(updatedCompanyInfo.getName(), updatedCompany.getName());
    }
}
