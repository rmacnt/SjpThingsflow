package com.sjp.framework.fragment;

import android.graphics.Rect;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public final class StringUtil {

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() <= 0;
    }

    public static int length(String text) {
        if (isEmpty(text) == true) return 0;
        return text.length();
    }

    public static String textTrim(String text) {
        if (text != null && text.length() > 0) {
            return text.trim();
        } else {
            return "";
        }
    }

    public static boolean contains(String src, String dst) {
        src = textTrim(src);
        dst = textTrim(dst);
        return src.contains(dst);
    }

    public static boolean equals(String text1, String text2) {
        text1 = textTrim(text1);
        text2 = textTrim(text2);
        return text1.equals(text2);
    }

    public static boolean startsWith(String value, String prefix) {
        boolean result = false;
        value = textTrim(value);
        prefix = textTrim(prefix);
        if (!isEmpty(value)) {
            if (!isEmpty(prefix)) {
                result = value.startsWith(prefix);
            }
        }
        return result;
    }

    public static byte[] getBytes(String data, String charset) {
        if (isEmpty(data) == true || isEmpty(charset) == true) {
            return null;
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data.getBytes();
    }

    public static Rect getTextBound(String data, TextView view) {
        Rect bounds = new Rect();
        view.getPaint().getTextBounds(data, 0, data.length(), bounds);
        return bounds;
    }
}
