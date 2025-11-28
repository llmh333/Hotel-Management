package org.example.hotel_management.constant;

public class ErrorMessageConstant {

    public static class Validator {
        public static final String FIELD_CANNOT_BLANK = "The field cannot blank";
    }

    public static class Auth {
        public static final String INVALID_USERNAME = "Username must contain only Latin letters and numbers";
        public static final String INVALID_PASSWORD = "Password must be more than 6 characters";
        public static final String INVALID_EMAIL = "Email must be a valid email address";
        public static final String INVALID_PHONE_NUMBER = "Phone number must be a valid phone number";
        public static final String INVALID_FULL_NAME = "Full name must be a valid full name";

        public static final String USERNAME_ALREADY_EXIST = "Username already exist";
        public static final String EMAIL_ALREADY_EXIST = "Email already exist";
        public static final String PHONE_NUMBER_ALREADY_EXIST = "Phone number already exist";
    }
}
