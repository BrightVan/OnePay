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
package com.pay.one.union.unionpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.unionpay.UPPayAssistEx;
import com.pay.one.core.IPayStrategy;
import com.pay.one.core.IPayCallback;
import com.pay.one.union.activity.UnionPayAssistActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @see <a href="https://open.unionpay.com/ajweb/help/file/techFile?productId=3">Des</a>
 */
public class UnionPay implements IPayStrategy<UnionPayEntity> {
    public static final String EXTRA_UNION_PAY_INFO = "unionPayInfo";
    private static IPayCallback sPayCallback;

    @Override
    public void pay(@NonNull Activity activity, @NonNull UnionPayEntity payInfo, IPayCallback payCallback) {
        sPayCallback = payCallback;
        Intent intent = new Intent(activity, UnionPayAssistActivity.class);
        intent.putExtra(EXTRA_UNION_PAY_INFO, payInfo);
        activity.startActivity(intent);
    }

    public static void pay(@NonNull Activity activity, @NonNull UnionPayEntity payEntity) {
        UPPayAssistEx.startPay(activity, null, null, payEntity.tn, payEntity.mode
                .getMode());
    }

    /*************************************************
     * 步骤3：处理银联手机支付控件返回的支付结果
     ************************************************/
    public static void handleResult(Activity activity, Intent data) {
        if (data == null) {
            activity.finish();
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase(UnionPayErrCode.RESPONSE_MESSAGE_SUCCESS)) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, "mode");
                    if (ret) {
                        // 验证通过后，显示支付结果
                        if (sPayCallback != null) {
                            sPayCallback.onSuccess();
                        }
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        if (sPayCallback != null) {
                            sPayCallback.onFailed(UnionPayErrCode.MESSAGE_VERIFY_ERROR);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                if (sPayCallback != null) {
                    sPayCallback.onSuccess();
                }
            }
        } else if (str.equalsIgnoreCase(UnionPayErrCode.RESPONSE_MESSAGE_FAIL)) {
            if (sPayCallback != null) {
                sPayCallback.onFailed(UnionPayErrCode.MESSAGE_FAIL);
            }
        } else if (str.equalsIgnoreCase(UnionPayErrCode.RESPONSE_MESSAGE_CANCEL)) {
            if (sPayCallback != null) {
                sPayCallback.onCancel();
            }
        }
        releaseUnionPayContext();
        activity.finish();
    }

    private static boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }

    private static void releaseUnionPayContext() {
        Field[] fields = UPPayAssistEx.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                if (field.getType() == Context.class) {
                    try {
                        field.setAccessible(true);
                        field.set(null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
