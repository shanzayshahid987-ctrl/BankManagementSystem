package security;

import java.security.*;
import exceptions.InvalidPasswordFormatException;

public class PasswordUtil {
    private String hashedPass;

    public PasswordUtil(String rawPassword) throws InvalidPasswordFormatException {
        if (!(this.isValidFormat(rawPassword))) {
            throw new InvalidPasswordFormatException(
                    "Password must be 8 characters, including number and special character");
        }
        this.hashedPass = hash(rawPassword);
    }

    public String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes());
            StringBuilder s = new StringBuilder();
            for (byte b : hashBytes) {
                s.append(String.format("%02x", b));
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.", e);
        }
    }

    public boolean isValidFormat(String rawPassword) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,12}$";
        return rawPassword.matches(pattern);
    }

    public boolean toVerify(String rawPassword) {
        String hashed = hash(rawPassword);
        return hashed.equals(this.hashedPass);
    }

    public String toString() {
        return this.hashedPass;
    }

}