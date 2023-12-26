package com.binghe.domain.validate;

public class IntergerValidation {

    private IntergerValidation() {
    }

    public static boolean validate(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
