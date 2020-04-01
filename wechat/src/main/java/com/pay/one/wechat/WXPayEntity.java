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
package com.pay.one.wechat;


import android.text.TextUtils;

import com.pay.one.core.IPayEntity;


public class WXPayEntity implements IPayEntity {
    /**
     * sign : ECE311C3DF76E009E6F37F05C350625F
     * timestamp : 1474886901
     * partnerid : 1391669502
     * package : Sign=WXPay
     * appid : wx46a24ab145becbde
     * nonceStr : 0531a4a42fa846fe8a7563847cd24c2a
     * prepayId : wx20160926184820acbd9357100240402425
     */

    public String sign;
    public String timestamp;
    public String partnerId;
    public String packageValue;
    public String appId;
    public String nonceStr;
    public String prepayId;

    public boolean isInvalid() {
        return TextUtils.isEmpty(sign) ||
                TextUtils.isEmpty(timestamp) ||
                TextUtils.isEmpty(partnerId) ||
                TextUtils.isEmpty(packageValue) ||
                TextUtils.isEmpty(appId) ||
                TextUtils.isEmpty(nonceStr) ||
                TextUtils.isEmpty(prepayId);
    }


}
