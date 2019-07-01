package com.company.models.employeesalary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling employee IO operations.
 */
public enum EmployeeFile {

    INSTANCE;

    private static final String EMPLOYEE_SALARY_FILE_NAME = "employees.dat";
    private RandomAccessFile employeeSalaryFile;
    private List<Employee> employeeDataList = new ArrayList<>();

    EmployeeFile() {
        linkEmployeeSalaryFile();
        loadEmployeeDataList();
    }

    /**
     * Links the employeeSalaryFile object with it's corresponding data file.
     * File will be created if it does not exist.
     */
    private void linkEmployeeSalaryFile() {
        try {
            employeeSalaryFile = new RandomAccessFile(EMPLOYEE_SALARY_FILE_NAME, "rw");
        }
        catch (FileNotFoundException e) {
            if (tryToCreateNewFile()) {
                linkEmployeeSalaryFile();
            }
        }
    }

    private boolean tryToCreateNewFile() {
        File file = new File(EMPLOYEE_SALARY_FILE_NAME);
        try {
            return file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Loads data from employeeSalaryFile onto employeeDataList
     */
    private void loadEmployeeDataList() {

        String line;
        try {
            while ((line = employeeSalaryFile.readLine()) != null) {
                Employee employee = parseEmployeeDataLine(line);
                employeeDataList.add(employee);
            }
        }
        catch (IOException ex) {
            System.out.println("Error when reading from file: " +   ex.getMessage());
        }

    }


    /**
     * Parses a line of the Employee's CSV-like file, into an actual Employee object.
     * @param s Line of data to be parsed
     * @return parsed Employee Object
     */
    private Employee parseEmployeeDataLine(String s) {

        final int EMPLOYEE_ID_INDEX = 0;
        final int EMPLOYEE_NAME_INDEX = 1;
        final int EMPLOYEE_SALARY_INDEX = 2;

        String[] dataPieces = s.split(";");

        int employeeId = Integer.parseInt(dataPieces[EMPLOYEE_ID_INDEX]);
        String employeeName = dataPieces[EMPLOYEE_NAME_INDEX];
        double employeeSalary = Double.parseDouble(dataPieces[EMPLOYEE_SALARY_INDEX]);

        return new Employee(employeeId, employeeName, employeeSalary);
    }

    /**
     * Writes an employee into RAM.
     * @param name name of the employee
     * @param salary salary of the employee
     */
    public void writeEmployee(String name, double salary) {
        int newEmployeeId = getNewEmployeeId();
        Employee newEmployee = new Employee(newEmployeeId, name, salary);
        employeeDataList.add(newEmployee);
    }

    /*
    Employee IDs are generated from adding +1 from the last employee's ID in the ArrayList.
     */
    private int getNewEmployeeId() {
        int lastEmployeeIndex = employeeDataList.size() - 1;

        if (lastEmployeeIndex == -1) {
            return 0;
        }

        return employeeDataList.get(lastEmployeeIndex).getId() + 1;
    }

    /**
     * Deletes an employee from RAM
     * @param employeeId id of employee to be deleted
     * @return Returns true upon success.
     */
    public boolean deleteEmployee(int employeeId) {
        int employeeIndex = getEmployeeIndexFromId(employeeId);

        if (employeeIndex == -1)
            return false;

        employeeDataList.remove(employeeIndex);
        return true;
    }

    /**
     * Finds an employee's index in the employeeDataList
     * @param employeeId id of the employee
     * @return index of the employee
     */
    private int getEmployeeIndexFromId(int employeeId) {
        for (int i = 0; i < employeeDataList.size(); i++) {
            Employee employee = employeeDataList.get(i);
            if (employee.getId() == employeeId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Saves current copy of employees onto the disk.
     */
    public void saveFileToDisk() {
        try {
            employeeSalaryFile.setLength(0);
            for (Employee employee : employeeDataList) {
                String parsableEmployeeDataLine = employee.getId() + ";" + employee.getName() + ";" + employee.getSalary();
                employeeSalaryFile.writeBytes(parsableEmployeeDataLine + "\r\n");
            }
        }
        catch (IOException ex) {
            System.out.println("Failed to save: " + ex.getMessage());
        }
    }

    public ArrayList<Employee> getDeepCopyOfEmployees() {
        List<Employee> copiedEmployeeDataList = new ArrayList<>();
        for (Employee employee : employeeDataList) {
            copiedEmployeeDataList.add(new Employee(employee.getId(), employee.getName(), employee.getSalary());
        }
        return copiedEmployeeDataList;
    }

    /**
     * Overwrites an employee
     * @param employee employee, bundled with its id, to be overwritten.
     * @return returns true if an edit was successfully made. false if employee id not found.
     */
    public boolean overwriteEmployee(Employee employee) {
        int employeeIndex = getEmployeeIndexFromId(employee.getId());
        if (employeeIndex == -1)
            return false;

        employeeDataList.set(employeeIndex, employee);

        return true;

    }
}
