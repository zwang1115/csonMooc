package com.cspiration.mooc.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashUtils {

    private static final HashFunction FUNCTION = Hashing.md5();

    private static final String SALT = "cspiration.com";

    public static String encryPassword(String password) {
        HashCode hashCode = FUNCTION.hashString(password + SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }
}
