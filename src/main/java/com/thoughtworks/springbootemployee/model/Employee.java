package com.thoughtworks.springbootemployee.model;

public class Employee {

    public static final int MIN_VALID_AGE = 18;
    public static final int MAX_VALID_AGE = 65;
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private Integer salary;
    private Long companyId;
    private boolean active = true;

    public Employee() {

    }

    public Employee(Long id, String name, Integer age, String gender, Integer salary, Long companyId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
        this.companyId = companyId;
    }

    public Employee(String name, Integer age, String gender, Integer salary, Long companyId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasInvalidAge() {
        return getAge() < MIN_VALID_AGE || getAge() > MAX_VALID_AGE;
    }
}
