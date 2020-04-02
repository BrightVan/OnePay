package com.pay.one.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * description: none
 * author: bright.van@qq.com
 * time: 2020/4/1
 * version: 1.0
 * update: none
 */
public abstract class IPayResultHandler {
    protected Context appContext;
    private static final String CONTEXT_NULL = "AppContext is NULL, cannot map code to string!";
    private static final String UNKNOWN = "unknown pay result!";

    public IPayResultHandler(Context appContext) {
        this.appContext = appContext;
    }

    @NonNull
    public String map(int code) {
        if (appContext == null) {
            return CONTEXT_NULL;
        }
        String handlerText = handle(code);
        return handlerText == null ? UNKNOWN : handlerText;
    }

    @Nullable
    protected abstract String handle(int code);
}
