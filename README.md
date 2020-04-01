### 使用步骤一、 集成依赖库
集成方式有以下两种，根据需要选择其中一种集成即可：

**远程依赖库集成方式**  Or **下载源码作为Module导入集成方式**；


#### 远程依赖库集成方式

在Project中主App模块中的build.gradle的dependencies块中添加以下依赖：

#### 0) 添加仓库:
allprojects {
    repositories {
        maven { url "https://raw.githubusercontent.com/BrightVan/OnePay/master" }
    }
}

#### 1) EasyPay支付基类库（必选）:
> 注意：本步骤必须添加，因为该库是EasyPay基类库

	implementation 'com.pay.one:core:1.0.0'

#### 2) 根据需要集成微信支付、支付宝支付、银联支付
> 注意：以下三个库可根据实际需要增删

##### 1）微信支付集成（可选）：

    implementation 'com.pay.one:wechat:1.0.0'

##### 2）支付宝支付集成（可选）：

    implementation 'com.pay.one:alipay:1.0.0'

##### 3）银联支付集成（可选）：

    implementation 'com.pay.one:union:1.0.0'

远程依赖集成方式到此结束。

#### 下载源码后作为module导入：

#### 1) 集成基类依赖库（必选）：
```
implementation project(':easypay')
```

#### 2) 根据需要集成其他支付依赖库

##### 1）微信支付集成（可选）：

```
implementation project(':wechatpay')
```
##### 2）支付宝集成（可选）：

```
implementation project(':alipay')
```
##### 3）银联支付集成（可选）：

```
implementation project(':unionpay')
```
下载源码作为Module导入集成方式到此结束。

------


### 使用步骤二、相关支付Api调用

#### 微信支付（共两步）
##### 1）配置
在项目主App模块的build.gradle文件的android{}块->defaultConfig{}块中配置applicationId,具体如下：
```
        manifestPlaceholders = [
                APPLICATION_ID: applicationId,
        ]
```
##### 2）api调用
```
    private void wxpay(){
        //实例化微信支付策略
        WXPay wxPay = WXPay.getInstance();
        //构造微信订单实体。一般都是由服务端直接返回。
        WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
        wxPayInfoImpli.setTimestamp("");
        wxPayInfoImpli.setSign("");
        wxPayInfoImpli.setPrepayId("");
        wxPayInfoImpli.setPartnerid("");
        wxPayInfoImpli.setAppid("");
        wxPayInfoImpli.setNonceStr("");
        wxPayInfoImpli.setPackageValue("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(wxPay, this, wxPayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(int code, String msg) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
    }
```
微信支付到此结束

#### 支付宝支付（共一步）
```
    private void alipay(){
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
        alipayInfoImpli.setOrderInfo("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(aliPay, this, alipayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(int code, String msg) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
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
        UnionPayInfoImpli unionPayInfoImpli = new UnionPayInfoImpli();
        unionPayInfoImpli.setTn("814144587819703061900");
        unionPayInfoImpli.setMode(Mode.TEST);
        //策略场景类调起支付方法开始支付，以及接收回调。
        OnePay.pay(unionPay, this, unionPayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                toast("支付成功");
            }

            @Override
            public void failed(int code, String msg) {
                toast("支付失败");
            }

            @Override
            public void cancel() {
                toast("支付取消");
            }
        });
    }
```

银联支付到此结束

------

## 框架扩展新的支付平台（如美团、京东等其他支付）
EasyPay从立项之初，就充分考虑了代码扩展性，启用策略模式，全部采用面向接口编程，遵循依赖倒置设计原则。从支付基类扩展出新的支付非常容易。仅需三步。下面给出参考步骤。更具体请参照项目中支付宝或者微信或者银联支付方式封装。
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
#### 1.0.0
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

