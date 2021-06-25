package com.github.thorbenkuck.scripting;

public interface VariableEvaluation {

    /*
     * Type checks
     */
    default boolean isVariable(String string, Register register) {
        return VariableEvaluation.isAVariable(string, register);
    }

    default boolean isString(String string) {
        return isAString(string);
    }

    default boolean isInteger(String s, Register register) {
        return isAnInteger(s, register);
    }

    default boolean isDouble(String s, Register register) {
        return isADouble(s, register);
    }

    default boolean isInteger(String s) {
        return isAnInteger(s);
    }

    default boolean isDouble(String s) {
        return isADouble(s);
    }

    /*
     * Type conversions
     */
    default String asStringValue(String value) {
        return asAStringValue(value);
    }

    default int asInt(String s) {
        return toAnInt(s);
    }

    default int toInt(String s, Register register) {
        return toAnInt(s, register);
    }

    default double asDouble(String s) {
        return toADouble(s);
    }

    default double toDouble(String s, Register register) {
        return toADouble(s, register);
    }

    /*
     * Globally available Methods
     */
    static boolean isAVariable(String string, Register register) {
        return !isAString(string) && !register.has(string);
    }

    static boolean isAString(String string) {
        return string.startsWith("\"") && string.endsWith("\"");
    }

    static String asAStringValue(String value) {
        return "\"" + value + "\"";
    }

    static boolean isAnInteger(String s, Register register) {
        return isAnInteger(s) || isAnInteger(register.get(s));
    }

    static boolean isAnInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    static boolean isAnInteger(String s) {
        return isAnInteger(s, 10);
    }

    static boolean isADouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean isADouble(String s, Register register) {
        return isADouble(s) || isADouble(register.get(s));
    }

    static double toADouble(String s) {
        return toADouble(s, 0.0);
    }

    static double toADouble(String s, double defaultValue) {
        if(!isADouble(s)) {
            return defaultValue;
        }
        return Double.parseDouble(s);
    }

    static double toADouble(String s, Register register) {
        return toADouble(s, register, 0.0);
    }

    static double toADouble(String s, Register register, double defaultValue) {
        if(!isADouble(s)) {
            if(isADouble(register.get(s))) {
                return toADouble(register.get(s));
            }
            if(isAnInteger(s, register)) {
                return toAnInt(s, register);
            }

            return defaultValue;
        }

        return toADouble(s);
    }

    static int toAnInt(String s) {
        return toAnInt(s, 0);
    }

    static int toAnInt(String s, int defaultValue) {
        if(!isAnInteger(s)) return defaultValue;

        return Integer.parseInt(s);
    }

    static int toAnInt(String s, Register register) {
        return toAnInt(s, register, 0);
    }

    static int toAnInt(String s, Register register, int defaultValue) {
        if(!isAnInteger(s)) {
            if(isAnInteger(s, register)) {
                return toAnInt(register.get(s));
            }
            return defaultValue;
        }

        return toAnInt(s);
    }
}
