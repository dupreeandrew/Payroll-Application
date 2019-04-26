package com.company.models.password;

import java.io.*;
import java.util.Scanner;

import static com.company.models.password.PasswordConfig.PASSWORD_FILE_NAME;

public class PasswordReader {

    private String actualPassword;

    public PasswordReader() {
        initActualPassword();
    }

    /**
     * loads the password from the password file onto actualPassword.
     */
    private void initActualPassword() {
        try (BufferedReader passwordReader = new BufferedReader(new FileReader(PASSWORD_FILE_NAME))){
            actualPassword = passwordReader.readLine();
        }
        catch (FileNotFoundException ex) {
            PasswordWriter.writeNewPassword(5);
            initActualPassword();
        }
        catch (IOException ex) {
            System.out.println("A password could not be generated.");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Blocking method awaiting for user's password input.
     * Unblocks upon correct password.
     */
    public void awaitForValidPassword() {

        Scanner passwordScanner = new Scanner(System.in);
        while (true) {
            String inputPassword = passwordScanner.nextLine();

            if (inputPassword.equals(actualPassword)) {
                return;
            }

            System.out.println("Invalid password, please try again.");

        }

    }
}
