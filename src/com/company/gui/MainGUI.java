package com.company.gui;

import com.company.models.employeesalary.Employee;
import com.company.models.employeesalary.EmployeeFile;
import com.company.models.employeesalary.EmployeeParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main GUI allowing users to add, remove, edit, and view currently selected employees.
 */
public class MainGUI extends JFrame {


    private EmployeeFile employeeFile = EmployeeFile.INSTANCE;
    private ArrayList<Employee> employeeList;
    private JList salaryList;

    /**
     * Static factory method for launching the main GUI.
     */
    public static void launch() {
        new MainGUI();
    }

    private MainGUI() {
        setTitle("Employee Salary List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        initSalaryList();
        add(buildGUI());

        pack();
        setVisible(true);

    }

    /**
     * Initializes salaryList with data from the employee file
     */
    private void initSalaryList() {
        employeeList = employeeFile.getDeepCopyOfEmployees();
        String[] employeeSalaryArray = EmployeeParser.parseEmployeesToCleanString(employeeList);
        salaryList = new JList<>(employeeSalaryArray);
        salaryList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        salaryList.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    /**
     * Create all the GUI Objects stored inside JPanel;
     */
    private JPanel buildGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(buildSalaryListScrollPanelFromSalaryListObject(), BorderLayout.WEST);
        mainPanel.add(buildButtonsPanel(), BorderLayout.EAST);
        return mainPanel;
    }

    /**
     * Returns a salary list panel from the pre-initialized salaryList
     */
    private JPanel buildSalaryListScrollPanelFromSalaryListObject() {
        JPanel salaryListPanel = new JPanel();
        JScrollPane scrollListPane = new JScrollPane(salaryList);
        salaryListPanel.add(scrollListPane);
        return salaryListPanel;
    }

    /**
     * Returns a buttons panel with all the core buttons needed (add/edit/delete)
     */
    private JPanel buildButtonsPanel() {

        GridBagConstraints gbc = buildGridBagConstraintsForButtons();


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout()); // allows for gbc to be used.
        buttonsPanel.add(buildAddEmployeeButton(), gbc);
        gbc.gridy++;
        buttonsPanel.add(buildEditEmployeeButton(), gbc);
        gbc.gridy++;
        buttonsPanel.add(buildDeleteEmployeeButton(), gbc);
        gbc.gridy++;
        return buttonsPanel;
    }

    private GridBagConstraints buildGridBagConstraintsForButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 5;
        return gbc;
    }



    /**
     * Creates add employee button.
     */
    private JButton buildAddEmployeeButton() {
        JButton addEmployeeButton = new JButton();
        addEmployeeButton.setText("Add Employee");
        addEmployeeButton.addActionListener(event
                -> AddEmployeeGUI.startForResult(this::refreshGUI));
        return addEmployeeButton;
    }

    private void refreshGUI() {
        setVisible(false);
        dispose();
        new MainGUI();
    }

    /**
     * Builds edit employee button
     */
    private JButton buildEditEmployeeButton() {
        JButton editEmployeeButton = new JButton();
        editEmployeeButton.setText("Edit Employee");
        editEmployeeButton.addActionListener(event -> {

            if (selectedIndexDoesNotExist()) {
                showNoSelectedIndexError();
                return;
            }

            Employee selectedEmployee = employeeList.get(salaryList.getSelectedIndex());
            EditEmployeeGUI.startForResult(selectedEmployee, this::refreshGUI);

        });

        return editEmployeeButton;
    }

    /**
     * @return true if user selected an item from the salaryList
     */
    private boolean selectedIndexDoesNotExist() {
        return salaryList.getSelectedIndex() == -1;
    }

    private void showNoSelectedIndexError() {
        final String ERROR_MESSAGE = "Please select an employee first";
        JOptionPane.showMessageDialog(null, ERROR_MESSAGE);
    }

    /**
     * Creates a delete employee button
     */
    private JButton buildDeleteEmployeeButton() {
        JButton deleteEmployeeButton = new JButton();
        deleteEmployeeButton.setText("Delete Employee");
        deleteEmployeeButton.addActionListener(event -> {

            if (selectedIndexDoesNotExist()) {
                showNoSelectedIndexError();
                return;
            }

            Employee selectedEmployee = employeeList.get(salaryList.getSelectedIndex());
            employeeFile.deleteEmployee(selectedEmployee.getId());
            employeeFile.saveFileToDisk();
            refreshGUI();
        });

        return deleteEmployeeButton;

    }

}
