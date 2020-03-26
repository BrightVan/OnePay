/*
 ******************************* Copyright (c)*********************************\
 **
 **                 (c) Copyright 2017, King, china
 **                          All Rights Reserved
 **
 **                               By(King)
 **
 **------------------------------------------------------------------------------
 */
package com.pay.one.alipay;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.pay.one.core.IPayStrategy;
import com.pay.one.core.IPayCallback;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @see <a href="https://docs.open.alipay.com/204/">Des</a>
 */
public class AliPay implements IPayStrategy<AliPayEntity> {

    @Override
    public void pay(Activity activity, AliPayEntity payInfo, IPayCallback payCallback) {
        /*sPayCallback = payCallback;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(aliPayInfoEntity.orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();*/
        AliPayTask aliPayTask = new AliPayTask(activity, payCallback);
        aliPayTask.execute(payInfo.orderInfo);
    }

    static class AliPayTask extends AsyncTask<String, Void, Map<String, String>> {
        private WeakReference<Activity> activityWeakReference;
        private IPayCallback payCallback;

        AliPayTask(Activity activity, IPayCallback payCallback) {
            activityWeakReference = new WeakReference<>(activity);
            this.payCallback = payCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 同步返回的结果必须放置到服务端进行验证
         * 验证的规则请看
         * https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1
         * 建议商户依赖异步通知
         * 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档:
         * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.IXE2Zj&treeId=59&articleId=103671&docType=1
         */
        @Override
        protected void onPostExecute(Map<String, String> result) {
            AliPayResult payResult = new AliPayResult(result);

            String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();

            if (TextUtils.equals(resultStatus, ResultCode.CODE_SUCCESS)) {
                if (payCallback != null) {
                    payCallback.success();
                }
            } else if (TextUtils.equals(resultStatus, ResultCode.CODE_CANCEL)) {
                if (payCallback != null) {
                    payCallback.cancel();
                }
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                if (payCallback != null) {
                    payCallback.failed(ResultCode.getTextByCode(resultStatus));
                }
            }
        }

        @Override
        protected void onCancelled() {
            payCallback.cancel();
        }

        @Override
        protected Map<String, String> doInBackground(String... orderInfo) {
            Activity activity = activityWeakReference.get();
            // 构造PayTask 对象
            PayTask payTask = new PayTask(activity);
            // 调用支付接口，获取支付结果
            return payTask.payV2(orderInfo[0], true);
        }
    }

}
