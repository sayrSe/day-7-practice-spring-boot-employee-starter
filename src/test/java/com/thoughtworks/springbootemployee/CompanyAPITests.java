package com.thoughtworks.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyAPITests {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MockMvc mockMvcClient;

    @BeforeEach
    void cleanupCompanyData() {
        companyRepository.cleanAll();
        employeeRepository.cleanAll();
    }

    @Test
    void should_return_all_companies_when_perform_get_companies() throws Exception {
        // Given
        Company company = companyRepository.addCompany(new Company("OOCL"));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(company.getId()))
                .andExpect(jsonPath("$[0].name").value(company.getName()));
    }

    @Test
    void should_return_the_company_when_perform_get_company_given_company_id() throws Exception {
        // Given
        Company company = companyRepository.addCompany(new Company("OOCL"));
        companyRepository.addCompany(new Company("Thoughtworks"));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/companies/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("OOCL"));
    }

    @Test
    void should_return_response_status_404_not_found_when_perform_get_company_given_non_existing_id() throws Exception {
        // Given
        long nonExistingId = 99L;

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/companies/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_employees_by_given_company_when_perform_get_employees() throws Exception {
        // Given
        Long otherCompanyId = 99L;
        Company company = companyRepository.addCompany(new Company("OOCL"));
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000, company.getId()));
        employeeRepository.addEmployee(new Employee(null, "Bob", 28, "Male", 8000, otherCompanyId));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/companies/" + company.getId() + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(alice.getId()))
                .andExpect(jsonPath("$[0].name").value(alice.getName()))
                .andExpect(jsonPath("$[0].age").value(alice.getAge()))
                .andExpect(jsonPath("$[0].gender").value(alice.getGender()))
                .andExpect(jsonPath("$[0].salary").value(alice.getSalary()));
    }

    @Test
    void should_return_new_company_when_perform_add_company_given_new_company() throws Exception {
        // Given
        Company company = companyRepository.addCompany(new Company("OOCL"));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(company)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value("OOCL"));
    }

    @Test
    void should_return_updated_company_when_perform_update_company_given_company_name() throws Exception {
        // Given
        Company company = companyRepository.addCompany(new Company("OOCL"));
        Company updateCompanyInfo = new Company("COSCO");

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.put("/companies/" + company.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(updateCompanyInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("COSCO"));
    }

    @Test
    void should_return_response_status_204_no_content_when_perform_delete_company_given_company_id() throws Exception {
        // Given
        Company company = companyRepository.addCompany(new Company("OOCL"));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.delete("/companies/" + company.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_paged_companies_when_perform_get_list_paged_companies_given_pageNumber_and_pageSize() throws Exception {
        // Given
        companyRepository.addCompany(new Company("Thoughtworks"));
        Company company = companyRepository.addCompany(new Company("OOCL"));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/companies")
                        .param("pageNumber", "2")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(company.getId()));
    }
}
