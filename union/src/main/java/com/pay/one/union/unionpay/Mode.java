package com.pay.one.union.unionpay;

public enum Mode {
    RELEASE("00"),
    TEST("01");
    private String mMode;

    Mode(String mode) {
        mMode = mode;
    }

    public String getMode() {
        return mMode;
    }
}
