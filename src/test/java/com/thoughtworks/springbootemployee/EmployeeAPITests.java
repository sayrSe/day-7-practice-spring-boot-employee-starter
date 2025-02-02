package com.thoughtworks.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.springbootemployee.model.Employee;
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
public class EmployeeAPITests {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MockMvc mockMvcClient;

    @BeforeEach
    void cleanupEmployeeData() {
        employeeRepository.cleanAll();
    }

    @Test
    void should_return_all_employees_when_perform_get_employees() throws Exception {
        // Given
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(alice.getId()))
                .andExpect(jsonPath("$[0].name").value(alice.getName()))
                .andExpect(jsonPath("$[0].age").value(alice.getAge()))
                .andExpect(jsonPath("$[0].gender").value(alice.getGender()))
                .andExpect(jsonPath("$[0].salary").value(alice.getSalary()));
    }

    @Test
    void should_return_the_employee_when_perform_get_employee_given_an_employee_id() throws Exception {
        // Given
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));
        employeeRepository.addEmployee(new Employee(null, "Bob", 28, "Male", 8000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/employees/" + alice.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(alice.getId()))
                .andExpect(jsonPath("$.name").value(alice.getName()))
                .andExpect(jsonPath("$.age").value(alice.getAge()))
                .andExpect(jsonPath("$.gender").value(alice.getGender()))
                .andExpect(jsonPath("$.salary").value(alice.getSalary()));
    }

    @Test
    void should_return_response_status_404_not_found_when_perform_get_employee_given_non_existing_id() throws Exception {
        // Given
        long nonExistingId = 99L;

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/employees/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_employees_by_given_gender_when_perform_get_employees() throws Exception {
        // Given
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));
        employeeRepository.addEmployee(new Employee(null, "Bob", 28, "Male", 8000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/employees").param("gender", "Female"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(alice.getId()))
                .andExpect(jsonPath("$[0].name").value(alice.getName()))
                .andExpect(jsonPath("$[0].age").value(alice.getAge()))
                .andExpect(jsonPath("$[0].gender").value(alice.getGender()))
                .andExpect(jsonPath("$[0].salary").value(alice.getSalary()));
    }

    @Test
    void should_return_new_employee_when_perform_add_employee_given_new_employee() throws Exception {
        // Given
        Employee newEmployee = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value("24"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.salary").value(9000));
    }

    @Test
    void should_return_updated_employee_when_perform_update_employee_given_employee_age_and_salary() throws Exception {
        // Given
        Employee alice = employeeRepository.addEmployee(new Employee(1L, "Alice", 24, "Female", 9000));
        Employee updatedEmployeeInfo = new Employee();
        updatedEmployeeInfo.setAge(30);
        updatedEmployeeInfo.setSalary(10000);

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.put("/employees/" + alice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(updatedEmployeeInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.salary").value(10000));
    }

    @Test
    void should_return_response_status_204_no_content_when_perform_delete_employee_given_an_employee_id() throws Exception {
        // Given
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.delete("/employees/" + alice.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_paged_employees_when_perform_get_list_paged_employees_given_pageNumber_and_pageSize() throws Exception {
        // Given
        employeeRepository.addEmployee(new Employee(null, "Bob", 28, "Male", 8000));
        Employee alice = employeeRepository.addEmployee(new Employee(null, "Alice", 24, "Female", 9000));

        // When, Then
        mockMvcClient.perform(MockMvcRequestBuilders.get("/employees")
                        .param("pageNumber", "2")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(alice.getId()))
                .andExpect(jsonPath("$[0].name").value(alice.getName()))
                .andExpect(jsonPath("$[0].age").value(alice.getAge()))
                .andExpect(jsonPath("$[0].gender").value(alice.getGender()))
                .andExpect(jsonPath("$[0].salary").value(alice.getSalary()));
    }
}
