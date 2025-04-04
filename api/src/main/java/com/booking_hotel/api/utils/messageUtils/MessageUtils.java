package com.booking_hotel.api.utils.messageUtils;

public class MessageUtils {
    // USER NOTIFIED MESSAGE
    public static final String USER_NOT_FOUND = "User not found.";

    // USERNAME VALIDATE MESSAGE
    public static final String USER_NAME_NULL = "Username cannot be empty";
    public static final String USER_NAME_RANGE = "Username must be 8-32 characters long";
    public static final String USER_NAME_EXISTED = "Username is already used with another account";
    public static final String BAN_USER_MESSAGE = "Your account has been locked for 10 minutes due to multiple incorrect password attempts";
    public static final String BANNED_USER_MESSAGE = "Your account has been locked";

    // PASSWORD VALIDATE MESSAGE
    public static final String PASSWORD_NULL = "Password cannot be empty";
    public static final String PASSWORD_RANGE = "Password must be 8-32 characters long";
    public static final String PASSWORD_FORMAT = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character";
    public static final String PASSWORD_INVALID = "Invalid password";
    public static final String PASSWORD_RESET = "Password Reset";
    public static final String PASSWORD_DIRECT_RESET = "Click here to reset your password: ";
    public static final String PASSWORD_DIRECT_RESET_LIMIT = "Reset link is valid for 60 seconds";
    public static final String PASSWORD_RESET_LINK_NOTIFICATION = "Reset link sent to email.";
    public static final String PASSWORD_CHANGE_SUCCESSFULLY = "Password changed successfully.";

    // EMAIL VALIDATE MESSAGE
    public static final String EMAIL_NULL = "Email cannot be empty";
    public static final String EMAIL_FORMAT = "Invalid email format";
    public static final String EMAIL_EXISTED = "Email is already used with another account";

    // TOKEN VALIDATE MESSAGE
    public static final String TOKEN_INVALID = "Invalid or expired token.";

    // ROLE VALIDATE MESSAGE
    public static final String ROLE_INVALID_MESSAGE = "Invalid role";

    // HOTEL VALIDATE MESSAGE
    public static final String NOT_FOUND_HOTEL_MESSAGE = "Hotel not found";

    // ROOM VALIDATE MESSAGE
    public static final String NOT_FOUND_ROOM_MESSAGE = "Room not found";
    public static final String DATE_INVALID_MESSAGE = "Check-out date must be after check-in date";
    public static final String NOT_AVAILABLE_ROOM_MESSAGE = "Room is not available";

    // BOOKING VALIDATE MESSAGE
    public static final String NOT_FOUND_BOOKING_MESSAGE = "Booking not found";
    public static final String COMPLETED_BOOKING_STATUS = "COMPLETED";
    public static final String CONFIRMED_BOOKING_STATUS = "CONFIRMED";
    public static final String PENDING_BOOKING_STATUS = "PENDING";
    public static final String SUBJECT_BOOKING_MESSAGE = "BOOKING CONFIRMED";

    // PAYMENT VALIDATE MESSAGE
    public static final String NOT_FOUND_PAYMENT_MESSAGE = "Payment not found";
    public static final String PAYMENT_MESSAGE = "Click link to payment details: ";
    public static final String PAYMENT_SUBJECT_MESSAGE = "CONFIRM PAYMENT";
    public static final String PAYMENT_CREATED_MESSAGE = "Payment session created";
    public static final String PAYMENT_SUCCESS_STATUS = "SUCCESS";
}
