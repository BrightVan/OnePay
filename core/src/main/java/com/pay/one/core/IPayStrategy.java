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

public interface IPayStrategy<IPayInfo> {
    void pay(Activity activity, IPayInfo payInfo, IPayCallback payCallback);
}
