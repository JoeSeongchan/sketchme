package com.dutaduta.sketchme.videoconference.service;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class RandomSessionIdGenerator {
    public String generate(){
        try {
            // 랜덤한 바이트 배열 생성
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[64];
            secureRandom.nextBytes(randomBytes);

            // SHA-256 해시 알고리즘 적용
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(randomBytes);

            // 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
