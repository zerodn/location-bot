package com.balofun.bot.util;

import java.security.MessageDigest;

public class StringUtils {
    public static long hashUrlToLong(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(url.getBytes());

            // Chuyển 8 byte đầu thành long (cắt bớt MD5 để lấy số)
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash = (hash << 8) | (digest[i] & 0xff);
            }
            return hash;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
