package com.aki.mcutils;

import java.util.List;

public class Utils {
    public static String getListPackage(List<String> strings, String space) {
        String s = "{\n";
        for(String s2 : strings) {
            s = space + s2 + ",\n";
        }
        s += "}";
        return s;
    }
}
