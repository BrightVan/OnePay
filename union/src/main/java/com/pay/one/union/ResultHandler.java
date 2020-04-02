package com.pay.one.union;

import android.content.Context;

import androidx.annotation.Nullable;

import com.pay.one.core.IPayResultHandler;

/**
 * description: none
 * author: bright.van@qq.com
 * time: 2020/4/2
 * version: 1.0
 * update: none
 */
public class ResultHandler extends IPayResultHandler {
    static final int CODE_SUCCESS = 0;
    static final int CODE_FAIL = -1;
    static final int CODE_CANCEL = -2;
    static final int CODE_VERIFY_ERROR = -3;

    static final String RESPONSE_MESSAGE_SUCCESS = "success";
    static final String RESPONSE_MESSAGE_FAIL = "fail";
    static final String RESPONSE_MESSAGE_CANCEL = "cancel";

    public ResultHandler(Context appContext) {
        super(appContext);
    }

    String mapCompat(String result) {
        int code = -1000000;
        switch (result.toLowerCase()) {
            case RESPONSE_MESSAGE_SUCCESS:
                code = CODE_SUCCESS;
                break;
            case RESPONSE_MESSAGE_FAIL:
                code = CODE_FAIL;
                break;
            case RESPONSE_MESSAGE_CANCEL:
                code = CODE_CANCEL;
                break;
        }
        return map(code);
    }

    @Nullable
    @Override
    protected String handle(int code) {
        switch (code) {
            case CODE_SUCCESS:
                return appContext.getString(R.string.pay_result_success);
            case CODE_FAIL:
                return appContext.getString(R.string.pay_result_failed);
            case CODE_CANCEL:
                return appContext.getString(R.string.pay_result_failed);
            case CODE_VERIFY_ERROR:
                return appContext.getString(R.string.pay_result_verify_error);
        }
        return null;
    }
}
