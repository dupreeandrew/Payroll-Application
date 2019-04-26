package com.company.gui;

import com.company.models.employeesalary.Employee;
import com.company.models.employeesalary.EmployeeFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;

class EditEmployeeGUI extends JFrame {

    private EmployeeFile employeeFile = EmployeeFile.INSTANCE;
    private GUIListener listener;
    private JTextField employeeNameTextField;
    private JTextField employeeSalaryTextField;
    private JPanel mainPanel = new JPanel();
    private Employee employee;
    private JLabel errorLabel = new JLabel();

    static void startForResult(Employee employee, GUIListener listener) {
        EditEmployeeGUI gui = new EditEmployeeGUI(employee, listener);
        gui.pack();
        gui.setVisible(true);
    }

    private EditEmployeeGUI(Employee employee, GUIListener listener) {

        this.employee = employee;
        this.listener = listener;

        setTitle("Edit employee");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main Panel Building
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        createEditEmployeeNameFields();
        createEditEmployeeSalaryFields();
        createMultiplySalaryButton();
        createApplyAddSubSalaryButton();

        createSubmitButton();

        // Error Label
        errorLabel.setText("* ");
        mainPanel.add(errorLabel);
        
        add(mainPanel);
    }

    /**
     * Creates label/textfield for employee name.
     */
    private void createEditEmployeeNameFields() {

        generateLabel("Enter employee name:");
        employeeNameTextField = new JTextField();
        employeeNameTextField.setColumns(10);
        employeeNameTextField.setText(employee.getName());
        mainPanel.add(employeeNameTextField);

    }

    private void generateLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        mainPanel.add(label);
    }


    /**
     * Creates label/textfield for employee salary.
     */
    private void createEditEmployeeSalaryFields() {
        generateLabel("Enter employee salary:");
        employeeSalaryTextField = new JTextField();
        employeeSalaryTextField.setColumns(7);
        
        String employeeSalaryString = new DecimalFormat("#.00").format(employee.getSalary());
        employeeSalaryTextField.setText(employeeSalaryString);
        
        mainPanel.add(employeeSalaryTextField);
    }

    private void createMultiplySalaryButton() {
        JButton multiplyButton = new JButton();
        multiplyButton.setText("Multiply salary");
        multiplyButton.addActionListener(event -> {
            try {
                double multiplier = openDialogForValue();
                tryMultiplySalaryField(multiplier);
            }
            catch (NumberFormatException ex) {
                final String ERROR_MESSAGE = "Please enter a valid multiplier.";
                errorLabel.setText(ERROR_MESSAGE);
            }
        });
        mainPanel.add(multiplyButton);
    }

    private void tryMultiplySalaryField(double multiplier) {
        double salary = DataValidation.getSalaryFromString(employeeSalaryTextField.getText());
        if (testSalaryInput(salary)) {
            employeeSalaryTextField.setText("$" + (salary * multiplier));
        }
    }

    /**
     * Checks if salary input is returning -1 or not.
     * Error message is thrown if so.
     * @return true if salary input is OK.
     */
    private boolean testSalaryInput(double salaryInput) {
        if (salaryInput == -1) {
            final String ERROR_MESSAGE = "* Please put a valid salary";
            errorLabel.setText(ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private double openDialogForValue() throws NumberFormatException {
        String input = JOptionPane.showInputDialog(null, "Enter Value: ");
        return Double.parseDouble(input);
    }

    private void createApplyAddSubSalaryButton() {
        JButton applyArithmeticButton = new JButton();
        applyArithmeticButton.setText("Apply Add/Sub");
        applyArithmeticButton.addActionListener(event -> {
            try {
                double delta = openDialogForValue();
                tryApplyAddSubToField(delta);
            }
            catch (NumberFormatException ex) {
                final String ERROR_MESSAGE = "Please enter a valid multiplier.";
                errorLabel.setText(ERROR_MESSAGE);
            }
        });
        mainPanel.add(applyArithmeticButton);
    }

    /**
     * Tries to apply addition/subraction to the salary text field.
     */
    private void tryApplyAddSubToField(double delta) {
        double salary = DataValidation.getSalaryFromString(employeeSalaryTextField.getText());
        if (testSalaryInput(salary))
            employeeSalaryTextField.setText("$" + (salary + delta));
    }

    private void createSubmitButton() {
        JButton submitButton = new JButton();
        submitButton.setText("Submit");
        mainPanel.add(submitButton);
        submitButton.addActionListener(e -> tryToSaveEmployeeOnFile());
    }

    private void tryToSaveEmployeeOnFile() {
        String nameInput = employeeNameTextField.getText();
        if (!DataValidation.validateName(nameInput)) {
            errorLabel.setText("* Please enter a valid name.");
            return;
        }

        double salary = DataValidation.getSalaryFromString(employeeSalaryTextField.getText());
        if (salary == -1) {
            errorLabel.setText("* Please enter an actual salary.");
            return;
        }
        
        if (overwriteEmployee(nameInput, salary)) {
            trashGUI();
            listener.onDataChanged();
            return;
        }

        // this should NEVER happen if the front end user only touches what's he's supposed to.
        final String ERROR_MESSAGE = "Employee could not be saved.";
        errorLabel.setText(ERROR_MESSAGE);

    }

    private boolean overwriteEmployee(String nameInput, double salary) {
        Employee editedEmployee = new Employee(employee.getId(), nameInput, salary);
        if (employeeFile.overwriteEmployee(editedEmployee)) {
            employeeFile.saveFileToDisk();
            return true;
        }

        return false;

    }

    private void trashGUI() {
        setVisible(false);
        dispose();
    }


}
