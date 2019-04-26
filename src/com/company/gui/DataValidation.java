package com.company.gui;

class DataValidation {
    /**
     * @return true if name input is a valid name.
     */
    static boolean validateName(String name) {
        return name.length() != 0;
    }

    /**
     * Given a string of money, this will convert it into a double
     * @param salaryText ex: $539.52 / 39.5 / $1,000.52
     * @return double representation of salary string. Returns -1 if invalid string input
     */
    static double getSalaryFromString(String salaryText) {

        salaryText = salaryText.replaceAll(",", ""); // deletes commas

        if (salaryText.indexOf('$') == 0) {
            salaryText = salaryText.substring(1);
        }

        try {
            return Double.parseDouble(salaryText);
        }
        catch (NumberFormatException ex) {
            return -1;
        }
    }
}
