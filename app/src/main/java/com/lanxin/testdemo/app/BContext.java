package com.lanxin.testdemo.app;

import android.content.Context;

public class BContext {
    private static Context context;

    private BContext() {
    }

    public static void init(Context context) {
        BContext.context = context;
    }

    public static Context context() {
        if (null == context) {
            throw new IllegalStateException("Please init before calling this");
        }
        return context;
    }

}
