package com.pay.one.union.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;

import com.pay.one.union.unionpay.UnionPay;
import com.pay.one.union.unionpay.UnionPayEntity;

/**
 * 银联辅助类。由于银联回调必须在onActivityResult中获取，为了使用方便，故拉起一个透明的activity来处理该问题。
 */
public class UnionPayAssistActivity extends AppCompatActivity {
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