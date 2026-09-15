package transactionpin;
import exceptions.*;
import java.security.*;

public class TransactionPIN {
    private String hashedPIN;

    public TransactionPIN(String rawPIN) throws InvalidPinException {
        if (!(isValidFormat(rawPIN))) {
            throw new InvalidPinException("PIN must be 4 digit only.");
        }
        this.hashedPIN = hash(rawPIN);
    }

    public String hash(String rawPIN) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPIN.getBytes());
            StringBuilder s = new StringBuilder();
            for (byte b : hashBytes) {
                s.append(String.format("%02x", b));
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.", e);
        }
    }

    public boolean isValidFormat(String rawPIN) {
        String pattern = "\\d{4}";
        return rawPIN.matches(pattern);
    }

    public boolean toVerify(String rawPIN) {
        String hashed = hash(rawPIN);
        return hashed.equals(this.hashedPIN);
    }
}