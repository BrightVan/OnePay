package com.pay.one.core;

import androidx.annotation.Nullable;

public interface IPayCallback {
    void success();
    void failed(@Nullable String message);
    void cancel();
}
