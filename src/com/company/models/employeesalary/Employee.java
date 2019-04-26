package com.company.models.employeesalary;

/**
 * Data class representing an employee
 */
public class Employee {
    private int id;
    private String name;
    private double salary;

    /**
     * Constructor for creating an employee.
     * @param id ID of employee
     * @param name name of employee
     * @param salary salary of employee
     */
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public double getSalary() {
        return salary;
    }

}
