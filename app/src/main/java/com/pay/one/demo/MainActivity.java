package com.pay.one.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.pay.one.alipay.AliPay;
import com.pay.one.alipay.AliPayEntity;
import com.pay.one.core.OnePay;
import com.pay.one.core.IPayCallback;
import com.pay.one.union.unionpay.Mode;
import com.pay.one.union.unionpay.UnionPay;
import com.pay.one.union.unionpay.UnionPayEntity;
import com.pay.one.wechat.WXPay;
import com.pay.one.wechat.WXPayEntity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.unionpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unionpay();
            }
        });
        findViewById(R.id.wxpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxpay();
            }
        });
        findViewById(R.id.alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipay();
            }
        });
    }

    /**
     * 银联提供了测试帐号：
     * 测试卡号信息：
     * 借记卡：6226090000000048
     * 手机号：18100000000
     * 密码：111101
     * 短信验证码：123456
     * （短信验证码记得点下获取验证码之后再输入）
     * 测试订单生成网址：http://101.231.204.84:8091/sim/getacptn，生成后直接传入setTn()。
     */
    private void unionpay() {
        //实例化银联支付策略
        UnionPay unionPay = new UnionPay();
        //构造银联订单实体。一般都是由服务端直接返回。测试时可以用Mode.TEST,发布时用Mode.RELEASE。
        UnionPayEntity unionPayEntity = new UnionPayEntity();
        unionPayEntity.tn = ("625623784203097901200");
        unionPayEntity.mode = (Mode.TEST);
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(unionPay, this, unionPayEntity, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(String message) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
    }

    private void wxpay() {
        //实例化微信支付策略
        WXPay wxPay = WXPay.getInstance();
        //构造微信订单实体。一般都是由服务端直接返回。
        WXPayEntity wxPayEntity = new WXPayEntity();
        wxPayEntity.timestamp = ("");
        wxPayEntity.sign = ("");
        wxPayEntity.prepayId = ("");
        wxPayEntity.partnerId = ("");
        wxPayEntity.appId = ("");
        wxPayEntity.nonceStr = ("");
        wxPayEntity.packageValue = ("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(wxPay, this, wxPayEntity, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(String message) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
    }

    private void alipay() {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AliPayEntity aliPayEntity = new AliPayEntity();
        aliPayEntity.orderInfo = ("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(aliPay, this, aliPayEntity, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(String message) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
    }

    private void toast(String context) {
        Toast.makeText(this, context, Toast.LENGTH_LONG).show();
    }
}
