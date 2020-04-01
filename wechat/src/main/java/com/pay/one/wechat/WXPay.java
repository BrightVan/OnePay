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

import android.app.Activity;
import android.content.Intent;

import com.pay.one.core.IPayResultHandler;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.pay.one.core.IPayStrategy;
import com.pay.one.core.IPayCallback;

/**
 * 文 件 名: WXPay
 * 创 建 人: King
 * 创建日期: 2017/2/13 19:03
 * 邮   箱: mikey1101@163.com
 * 博   客: www.smilevenus.com
 *
 * @see <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN">Des</a>
 */
public class WXPay implements IPayStrategy<WXPayEntity> {
    private static WXPay mWXPay;
    private WXPayEntity payInfoEntity;
    private IPayCallback sPayCallback;
    private IWXAPI mWXApi;
    private IPayResultHandler resultHandler;

    private WXPay() {
    }

    //这里之所以单利模式，是为了
    public static WXPay getInstance() {
        if (mWXPay == null) {
            synchronized (WXPay.class) {
                if (mWXPay == null) {
                    mWXPay = new WXPay();
                }
            }
        }
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    @Override
    public void pay(Activity activity, WXPayEntity payInfo, IPayCallback payCallback) {
        this.payInfoEntity = payInfo;
        sPayCallback = payCallback;
        resultHandler = new ResultHandler(activity.getApplicationContext());

        if (payInfoEntity == null || payInfoEntity.isInvalid()) {
            if (payCallback != null) {
                payCallback.onFailed(resultHandler.map(ResultHandler.CODE_ILLEGAL_PARAMS));
            }
            return;
        }

        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(activity.getApplicationContext(), payInfoEntity.appId);
            //mWXApi.registerApp(payInfoEntity.appId);
        }

        if (!check()) {
            if (payCallback != null) {
                payCallback.onFailed(resultHandler.map(ResultHandler.CODE_UN_SUPPORT));
            }
            return;
        }
        //启动没有界面的activity 用于接收微信回调
        activity.startActivity(new Intent(activity, WXPayActivity.class));

        PayReq req = new PayReq();
        req.appId = payInfoEntity.appId;
        req.partnerId = payInfoEntity.partnerId;
        req.prepayId = payInfoEntity.prepayId;
        req.packageValue = payInfoEntity.packageValue;
        req.nonceStr = payInfoEntity.nonceStr;
        req.timeStamp = payInfoEntity.timestamp;
        req.sign = payInfoEntity.sign;

        mWXApi.sendReq(req);
    }

    /**
     * 支付回调响应
     */
    public void onResp(int errorCode, String errorMsg) {
        if (sPayCallback == null) {
            return;
        }

        if (errorCode == BaseResp.ErrCode.ERR_OK) {
            sPayCallback.onSuccess();
        } else if (errorCode == BaseResp.ErrCode.ERR_COMM) {
            sPayCallback.onFailed(errorMsg);
        } else if (errorCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            sPayCallback.onCancel();
        } else {
            sPayCallback.onFailed(errorMsg);
        }

        sPayCallback = null;
    }

    /**
     * 检测是否支持微信支付
     */
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}
