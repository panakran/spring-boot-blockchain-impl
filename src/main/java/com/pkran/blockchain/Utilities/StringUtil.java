package com.pkran.blockchain.Utilities;

import java.security.Key;
import java.util.Base64;

public final class StringUtil {

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String createZerosStringFromInteger(Integer integer){
        return new String(new char[integer]).replace('\0', '0'); //Create a string with difficulty * "0"
    }
}
