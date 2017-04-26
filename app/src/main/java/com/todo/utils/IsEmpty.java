package com.todo.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by dzq on 16/7/21.
 */
public class IsEmpty {
    public static boolean list(List object) {
        if (null == object || object.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean list(Set object) {
        if (null == object || object.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean string(String object) {
        if (null == object || object.trim().length() == 0 || "".equals(object)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean object(Object object) {
        if (null == object) {
            return true;
        } else {
            return false;
        }
    }

}
