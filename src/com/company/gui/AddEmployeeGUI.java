package com.company.gui;

import com.company.models.employeesalary.EmployeeFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

class AddEmployeeGUI extends JFrame {

    private GUIListener listener;
    private JTextField employeeNameTextField;
    private JTextField employeeSalaryTextField;
    private JLabel errorLabel = new JLabel();
    private JPanel mainPanel = new JPanel();

    /**
     * Static factory method for launching a GUI for adding an employee
     * @param listener Listener to be called when a change in data was made.
     */
    static void startForResult(GUIListener listener) {
        AddEmployeeGUI gui = new AddEmployeeGUI(listener);
        gui.pack();
        gui.setVisible(true);
    }

    private AddEmployeeGUI(GUIListener listener) {

        this.listener = listener;

        setTitle("Add an employee");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        createEmployeeNameFields();
        createEmployeeSalaryFields();
        createSubmitButton();

        errorLabel.setText("* ");
        mainPanel.add(errorLabel);
        add(mainPanel);
    }

    /**
     * Creates label/textfield for employee's name on mainPanel
     */
    private void createEmployeeNameFields() {
        generateLabel("Enter employee name:");
        employeeNameTextField = new JTextField();
        employeeNameTextField.setColumns(10);
        mainPanel.add(employeeNameTextField);
    }

    /**
     * Creates a label, adding it onto mainPanel immediately
     */
    private void generateLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        mainPanel.add(label);
    }

    /**
     * Creates label/textfield for employee's salary on mainPanel
     */
    private void createEmployeeSalaryFields() {
        generateLabel("Enter employee salary:");
        employeeSalaryTextField = new JTextField();
        employeeNameTextField.setColumns(7);
        mainPanel.add(employeeSalaryTextField);
    }

    private void createSubmitButton() {
        JButton submitButton = new JButton();
        submitButton.setText("Add employee");
        mainPanel.add(submitButton);
        submitButton.addActionListener(e -> {

            if (tryToSaveEmployeeOnFile()) {
                listener.onDataChanged();
                trashGUI();
            }

        });
    }

    /**
     * Attempts to save user input data as an employee on the file.
     * @return returns true on success.
     */
    private boolean tryToSaveEmployeeOnFile() {
        String nameInput = employeeNameTextField.getText();
        if (!DataValidation.validateName(nameInput)) {
            errorLabel.setText("* Please enter a valid name.");
            return false;
        }

        double salary = DataValidation.getSalaryFromString(employeeSalaryTextField.getText());
        if (salary == -1) {
            errorLabel.setText("* Please enter an actual salary.");
            return false;
        }

        EmployeeFile employeeFile = EmployeeFile.INSTANCE;
        employeeFile.writeEmployee(nameInput, salary);
        employeeFile.saveFileToDisk();

        return true;

    }

    private void trashGUI() {
        setVisible(false);
        dispose();
    }

}
