package com.company.models.password;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static com.company.models.password.PasswordConfig.PASSWORD_FILE_NAME;

class PasswordWriter {
    static void writeNewPassword(int length) {
        try {
            FileWriter passwordWriter = new FileWriter(PASSWORD_FILE_NAME);
            String randomPassword = generateNewPassword(length);
            passwordWriter.write(randomPassword);
            passwordWriter.close();

            System.out.println("New password was generated, please write it down:");
            System.out.println(randomPassword);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateNewPassword(int length) {

        final int MIN_ASCII_DEC = 48;
        final int MAX_ASCII_DEC = 122;
        final int ASCII_RANGE = MAX_ASCII_DEC - MIN_ASCII_DEC;

        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i <= length; i++) {
            int randomCharInt = MIN_ASCII_DEC + random.nextInt(ASCII_RANGE);
            passwordBuilder.append((char)randomCharInt);
        }

        return passwordBuilder.toString();

    }
}
