package com.pay.one.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IPayCallback {
    void onSuccess();

    void onFailed(@NonNull String message);

    void onCancel();
}
