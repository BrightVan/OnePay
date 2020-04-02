package com.pay.one.alipay;

import android.content.Context;

import com.pay.one.core.IPayResultHandler;

/**
 * description: none
 * author: bright.van@qq.com
 * time: 2020/4/1
 * version: 1.0
 * update: none
 */
class ResultHandler extends IPayResultHandler {
    static final int CODE_SUCCESS = 9000;
    static final int CODE_HANDLING = 8000;
    static final int CODE_FAILED = 4000;
    static final int CODE_REPEAT = 5000;
    static final int CODE_CANCEL = 6001;
    static final int CODE_NETWORK = 6002;
    static final int CODE_UNKNOWN = 6004;

    ResultHandler(Context appContext) {
        super(appContext);
    }


    @Override
    protected String handle(int code) {
        switch (code) {
            case CODE_SUCCESS:
                return appContext.getString(R.string.pay_result_success);
            case CODE_FAILED:
                return appContext.getString(R.string.pay_result_failed);
            case CODE_HANDLING:
                return appContext.getString(R.string.pay_result_handling);
            case CODE_REPEAT:
                return appContext.getString(R.string.pay_result_repeat);
            case CODE_CANCEL:
                return appContext.getString(R.string.pay_result_cancel);
            case CODE_NETWORK:
                return appContext.getString(R.string.pay_result_network);
            case CODE_UNKNOWN:
                return appContext.getString(R.string.pay_result_unknown);
        }
        return null;
    }
}
