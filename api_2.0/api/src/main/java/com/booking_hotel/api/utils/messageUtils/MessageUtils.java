package com.booking_hotel.api.utils.messageUtils;

public class MessageUtils {
    // USER NOTIFIED MESSAGE
    public static final String USER_NOT_FOUND = "User not found.";

    // USERNAME VALIDATE MESSAGE
    public static final String USER_NAME_NULL = "Username không được để trống";
    public static final String USER_NAME_RANGE = "Username phải có độ dài từ 8 đến 32 ký tự";
    public static final String USER_NAME_EXISTED = "Username is already used with another account";

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
}
