package com.hhnatsiuk.api_auth_service.util;

import java.security.SecureRandom;

public class OtpGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;

    /** Генерирует цифровой код длиной length (с лидирующими нулями). */
    public static String generate(int length) {
        int maxValue = (int) Math.pow(10, length);
        int otp = random.nextInt(maxValue);
        return String.format("%0" + length + "d", otp);
    }

    /** Генерирует 6-значный код по умолчанию. */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

}
