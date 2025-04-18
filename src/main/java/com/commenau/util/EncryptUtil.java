package com.commenau.util;

import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EncryptUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    public static String encrypt(String input) {
        byte[] encodedBytes = Base64.encodeBase64(input.getBytes());
        return new String(encodedBytes);
    }

    public static String decrypt(String input) {
        byte[] decodedBytes = Base64.decodeBase64(input.getBytes());
        return new String(decodedBytes);
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println(hashPassword("123456"));
        }
    }
}
