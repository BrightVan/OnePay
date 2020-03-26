/*
 ******************************* Copyright (c)*********************************\
 **
 **                 (c) Copyright 2017, King, china
 **                          All Rights Reserved
 **
 **                              By(King)
 **
 **------------------------------------------------------------------------------
 */
package com.pay.one.core;

import android.app.Activity;

public final class OnePay {
    public static <IPayInfo> void pay(IPayStrategy<IPayInfo> payWay, Activity mActivity, IPayInfo payinfo, IPayCallback callback) {
        payWay.pay(mActivity, payinfo, callback);
    }
}
