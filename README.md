### 简介
> 支付接入，也没什么难得，平台做好了，调用即可。和原作者的代码相比，改动太多了，就不pull merge request了。
> 银联的自己目前没用，就没怎么重构，暂不支持
#### 和原项目相比：
- 迁移至androidx
- 去除大量无关资源和配置
- 增加多语言支持
- 优化错误处理
- 支持混淆规则自动配置（依赖包已经包含了，接入时不需要处理）
- 简化微信支付使用步骤

### 使用步骤一、 集成依赖库
集成方式有以下两种，根据需要选择其中一种集成即可：

**远程依赖库集成方式**  Or **下载源码作为Module导入集成方式**；


#### 远程依赖库集成方式

在Project中主App模块中的build.gradle的dependencies块中添加以下依赖：

#### 0) 添加仓库:
```
allprojects {
    repositories {
        maven { url "https://raw.githubusercontent.com/BrightVan/OnePay/master/archives" }
    }
}
```
#### 1) OnePay（必选）:
> 注意：本步骤必须添加，因为该库是OnePay基类库

	implementation 'com.pay.one:core:0.1.1'

#### 2) 根据需要集成微信支付、支付宝支付、银联支付
> 注意：以下三个库可根据实际需要增删

##### 1）微信支付集成（可选）：

    implementation 'com.pay.one:wechat:0.1.1'

##### 2）支付宝支付集成（可选）：

    implementation 'com.pay.one:alipay:0.1.0'

##### 3）银联支付集成（可选）：

    implementation 'com.pay.one:union:0.0.9'

远程依赖集成方式到此结束。

------


### 使用步骤二、相关支付Api调用

#### 微信支付（共一步）

```
    private void wxpay(){
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
            public void onSuccess() {
                toast("支付成功");
            }

            @Override
            public void onFailed(@NonNull String message) {
                toast("支付失败");
            }

            @Override
            public void onCancel() {
                toast("支付取消");
            }
        });
    }
```
微信支付到此结束

#### 支付宝支付（共2步）
>由于支付宝的SDK没有提供云端仓库的依赖方式，只能手动下载SDK文件依赖。且SDK为aar文件。
> maven打包不进去，所以需要手动下载支付宝sdk到项目进行依赖。
> 你可以到支付宝开放平台自己下载最新版，也可以在本项目alipay/libs目录下载

```
    private void alipay(){
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AliPayEntity aliPayEntity = new AliPayEntity();
        aliPayEntity.orderInfo = ("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(aliPay, this, aliPayEntity, new IPayCallback() {
            @Override
            public void onSuccess() {
                toast("支付成功");
            }

            @Override
            public void onFailed(@NonNull String message) {
                toast("支付失败");
            }

            @Override
            public void onCancel() {
                toast("支付取消");
            }
        });
    }
```
支付宝支付到此结束

#### 银联支付（共一步）
```
    private void unionpay(){
        //实例化银联支付策略
        UnionPay unionPay = new UnionPay();
        //构造银联订单实体。一般都是由服务端直接返回。测试时可以用Mode.TEST,发布时用Mode.RELEASE。
        UnionPayEntity unionPayEntity = new UnionPayEntity();
        unionPayEntity.tn = ("625623784203097901200");
        unionPayEntity.mode = (Mode.TEST);
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(unionPay, this, unionPayEntity, new IPayCallback() {
            @Override
            public void onSuccess() {
                toast("支付成功");
            }

            @Override
            public void onFailed(@NonNull String message) {
                toast("支付失败");
            }

            @Override
            public void onCancel() {
                toast("支付取消");
            }
        });
    }
```

银联支付到此结束

------

## 框架扩展新的支付平台（如美团、京东等其他支付）
### 1) 支付订单信息类实现IPayInfo接口
```
public class XXpayInfoImpli implements IPayInfo {
    public xxType xxField = xxx;
    public yyTYpe xxFiled = yyy;
    ...other Field
}
```
### 2) 支付策略类实现IPayStrategy。
将第一步中支付实体类传入泛型。支付策略的初衷是将某种支付所有操作都进行集中封装，凡是业务需要用到该支付的地方，都调用这个类即可。
```
public class XXPay implements IPayStrategy<XXpayInfoImpli> {
    private AlipayInfoImpli alipayInfoImpli;
    private static IPayCallback sPayCallback;

    @Override
    public void pay(Activity activity, AlipayInfoImpli payInfo, IPayCallback payCallback) {
        this.mActivity = activity;
        this.alipayInfoImpli = payInfo;
        sPayCallback = payCallback;
    }

    ...other method
}
```
完成上述两步后，根据业务在需要地方调用即可，需要注意是当某支付平台支付回调比较分散时，可在对应地方将调用转发给上述支付类即可。这样，可以将逻辑集中到一个类处理。如不理解这段话，可以看银联支付UnionPayAssistActivity中的onActivityResult()方法，就将逻辑转给 UnionPay.handleResult(this,data)处理了。

### 3）调用Api
```
        //实例化支付策略
        XXpay xxPay = new XXPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        XXpayInfoImpli xxpayInfoImpli = new XXpayInfoImpli();
        xxpayInfoImpli.setXXFiled();
        ...
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(xxPay, this, xxpayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(int code, String message) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
```
扩展介绍到此结束

------

## (ChangeLog) 更新日志
#### 0.1.1
- 修复微信模块添加releasePayCallback方法，防止内存泄露。
- 修复core模块，混淆规则问题

#### 0.1.0
- 修复微信模块AgentActivity不能实例化问题
- 修复core混淆配置错误问题

#### 0.0.9
- 更新微信支付/支付宝支付SDK
- 移除无关资源文件和配置文件
- 重构项目
- 结果处理，增加多语言支持
- 优化结果错误处理
- 简化微信支付接入，不需要继承某个Activity 或是 配置回调
------

## 联系我

#### 1) 有问题提[Issues](https://github.com/BrightVan/OnePay/issues)。欢迎大家交流想法。

#### 2) 邮箱联系(Email : bright.van#qq.com)

感谢大家，希望一起起步。

------

