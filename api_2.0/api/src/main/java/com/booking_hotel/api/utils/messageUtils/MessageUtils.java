package com.booking_hotel.api.utils.messageUtils;

public class MessageUtils {
    // USER NOTIFIED MESSAGE
    public static final String USER_NOT_FOUND = "User not found.";

    // USERNAME VALIDATE MESSAGE
    public static final String USER_NAME_NULL = "Username không được để trống";
    public static final String USER_NAME_RANGE = "Username phải có độ dài từ 8 đến 32 ký tự";
    public static final String USER_NAME_EXISTED = "Username is already used with another account";
    public static final String BAN_USER_MESSAGE = "Your account has been locked for 10 minutes due to entering the wrong password too many times.";
    public static final String BANNED_USER_MESSAGE = "Your account has been locked";
    // PASSWORD VALIDATE MESSAGE
    public static final String PASSWORD_NULL = "Password không được để trống";
    public static final String PASSWORD_RANGE = "Password phải có độ dài từ 8 đến 32 ký tự";
    public static final String PASSWORD_FORMAT = "Password phải chứa ít nhất một chữ hoa, một chữ thường, một số và một ký tự đặc biệt";
    public static final String PASSWORD_INVALID = "Invalid password";
    public static final String PASSWORD_RESET = "Password Reset";
    public static final String PASSWORD_DIRECT_RESET = "Click here to reset your password: ";
    public static final String PASSWORD_DIRECT_RESET_LIMIT = "reset link is valid for 60 seconds";
    public static final String PASSWORD_RESET_LINK_NOTIFICATION = "Reset link sent to email.";
    public static final String PASSWORD_CHANGE_SUCCESSFULLY = "Password changed successfully.";

    // EMAIL VALIDATE MESSAGE
    public static final String EMAIL_NULL = "Email không được để trống";
    public static final String EMAIL_FORMAT = "Email phải hợp lệ";
    public static final String EMAIL_EXISTED = "Email is already used with another account";

    // TOKEN VALIDATE MESSAGE
    public static final String TOKEN_INVALID = "Invalid or expired token.";

    // ROLE VALIDATE MESSAGE
    public static final String ROLE_INVALID_MESSAGE = "Invalid role";

    // HOTEL VALIDATE MESSAGE
    public static final String NOT_FOUND_HOTEL_MESSAGE = "Hotel not found";

    // ROOM VALIDATE MESSAGE
    public static final String NOT_FOUND_ROOM_MESSAGE = "Room not found";
    public static final String DATE_INVALID_MESSAGE = "Check out date must be after check in date";
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
