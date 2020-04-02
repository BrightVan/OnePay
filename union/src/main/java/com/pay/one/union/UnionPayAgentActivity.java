package com.pay.one.union;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * 银联辅助类。由于银联回调必须在onActivityResult中获取，为了使用方便，故拉起一个透明的activity来处理该问题。
 */
public class UnionPayAgentActivity extends Activity {
    UnionPayEntity mUnionPayEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mUnionPayEntity = getIntent().getParcelableExtra(UnionPay.EXTRA_UNION_PAY_INFO);
        if (mUnionPayEntity == null) {
            finish();
        } else {
            UnionPay.pay(this, mUnionPayEntity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UnionPay.handleResult(this, data);
    }
}
