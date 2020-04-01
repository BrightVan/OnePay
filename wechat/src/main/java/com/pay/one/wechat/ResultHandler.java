package com.pay.one.wechat;

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
    static final int CODE_UN_SUPPORT = 1000;
    static final int CODE_ILLEGAL_PARAMS = 1001;

    ResultHandler(Context appContext) {
        super(appContext);
    }

    @Override
    protected String handle(int code) {
        switch (code) {
            case CODE_UN_SUPPORT:
                return appContext.getString(R.string.pay_result_un_support);
            case CODE_ILLEGAL_PARAMS:
                return appContext.getString(R.string.pay_result_illegal_params);
        }
        return null;
    }
}
