package com.company.models.employeesalary;

import java.text.DecimalFormat;
import java.util.List;

public class EmployeeParser {
    /**
     * Get a parsed copy of all employee data.
     * @return ex: #52 Andrew: $537.22
     */
    public static String[] parseEmployeesToCleanString(List<Employee> employeeDataList) {
        int dataListSize = employeeDataList.size();
        String[] parsedEmployeeDataLines = new String[dataListSize];
        for (int i = 0; i < dataListSize; i++) {
            Employee employee = employeeDataList.get(i);
            String formattedSalary = new DecimalFormat("#.00").format(employee.getSalary());
            String parsedLine = "#" + employee.getId() + " " + employee.getName() + ": $" + formattedSalary;
            parsedEmployeeDataLines[i] = parsedLine;
        }
        return parsedEmployeeDataLines;
    }
}
