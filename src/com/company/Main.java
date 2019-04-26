package com.company;

import com.company.gui.MainGUI;
import com.company.models.password.PasswordReader;

public class Main {

    public static void main(String[] args) {

        System.out.println("Welcome to the payroll application.");

        System.out.println("Please enter the password.");
        PasswordReader passwordReader = new PasswordReader();
        passwordReader.awaitForValidPassword();

        MainGUI.launch();
    }
}
