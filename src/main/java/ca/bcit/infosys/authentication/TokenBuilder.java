package ca.bcit.infosys.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

public class TokenBuilder {
    private static final String ALGORITHM = "SHA-256";
    private MessageDigest messageDigest;

    public TokenBuilder() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance(ALGORITHM);
    }

    /**
     * Encrypts password string into a byte array
     * @param password to be encrypted
     * @return hashed password in byte array
     */
    public byte[] encrypt(String pw) {
        final byte[] pwArr = pw.getBytes(StandardCharsets.UTF_8);
        byte[] hashed = new byte[] {};
        try {
            hashed = messageDigest.digest(pwArr);
        } finally {
            Arrays.fill(pwArr, (byte) 0);
        }
        return hashed;
    }

    /**
     * Validates if incoming hash is the same as the actual password
     * @param incoming hash
     * @param actual password
     * @return true if match, else false
     */
    public boolean validate(String incoming, String actual) {
        return incoming.equals(actual);
    }

}
