package org.example.hotel_management.util;

import org.junit.jupiter.api.Test;

public class ValidatorUtilTest {

    @Test
    public void testValidPhoneNumber() {
        String phoneNumber = "0123456789";
        String isValid = ValidatorUtil.validate(phoneNumber);
        System.out.println(isValid);
    }
}
