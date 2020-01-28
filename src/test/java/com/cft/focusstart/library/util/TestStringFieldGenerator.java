package com.cft.focusstart.library.util;

public class TestStringFieldGenerator {
    public static String getWrongNull() {
        return null;
    }

    public static String getRightNull() {
        return null;
    }

    public static String getToLittle(int minLen) {
        String field = "";
        for(int i = 0; i < (minLen - 1); i++) {
            field += "s";
        }
        return field;
    }

    public static String getToBig(int maxLen) {
        String field = "";
        for(int i = 0; i < (maxLen + 2); i++) {
            field += "s";
        }
        return field;
    }

    public static String getRightByMin(int minLen) {
        String field = "";
        for(int i = 0; i <= minLen; i++) {
            field += "s";
        }
        return field;
    }

    public static String getRightByMax(int maxLen) {
        String field = "";
        for(int i = 0; i < maxLen; i++) {
            field += "s";
        }
        return field;
    }
}
