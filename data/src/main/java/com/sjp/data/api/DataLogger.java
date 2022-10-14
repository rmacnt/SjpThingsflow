package com.sjp.data.api;

import android.util.Log;


public final class DataLogger {

    private final static boolean enable = true;

    public static void i(String tag, String message) {
        if (enable) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (enable) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (enable) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (enable) {
            Log.w(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (enable) {
            Log.v(tag, message);
        }
    }

    public static void i(Tag tag, String message) {
        if (enable) {
            Log.i(tag.toString(), message);
        }
    }

    public static void d(Tag tag, String message) {
        if (enable) {
            Log.d(tag.toString(), message);
        }
    }

    public static void e(Tag tag, String message) {
        if (enable) {
            Log.e(tag.toString(), message);
        }
    }

    public static void w(Tag tag, String message) {
        if (enable) {
            Log.w(tag.toString(), message);
        }
    }

    public static void v(Tag tag, String message) {
        if (enable) {
            Log.v(tag.toString(), message);
        }
    }

    public static void printStackTrace(Exception e) {
        if (enable) {
            e.printStackTrace();
        }
    }

    public static void printStackTrace(Throwable e) {
        if (enable) {
            e.printStackTrace();
        }
    }

    public enum Tag {
        DATA_LOCAL,
        DATA_ERROR,
        DATA_REQUEST,
        DATA_RESPONSE,
    }
}
