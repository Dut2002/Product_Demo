package com.example.demo_oracle_db.util;

import java.security.SecureRandom;

public class PasswordUtil {
    // Mảng chứa các ký tự có thể xuất hiện trong mật khẩu
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

    // Hàm tạo mật khẩu ngẫu nhiên
    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom(); // Sử dụng SecureRandom để tạo số ngẫu nhiên an toàn
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index)); // Thêm ký tự ngẫu nhiên vào mật khẩu
        }

        return password.toString();
    }
}
