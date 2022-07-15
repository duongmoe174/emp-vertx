package com.example.starter.model;

public class Employee {
    private String id;
    private String fullName;
    @Override
    public String toString() {
        return "Employee [id=" + id + ", fullName=" + fullName + "]";
    }
    public Employee() {
    }
    public Employee(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return fullName;
    }
    public void setName(String fullName) {
        this.fullName = fullName;
    }

    
}
