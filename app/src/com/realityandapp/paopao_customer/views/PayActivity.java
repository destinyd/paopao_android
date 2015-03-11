package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.PayResult;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.SignUtils;
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by dd on 14-6-12.
 */
public class PayActivity extends PaopaoBaseActivity {
    public static final String PARTNER = "2088811746881150";
    public static final String SELLER = "ali@yishipa.com";
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANNso5dFWzYqi5JBjrv11Vjh9BhcbmsUP/bYU1b59oVbLHNCLaLn9poQtvUZSrlWx10WOkx+vy7eN4di8IIfJ4T0Qh6yM0JygNvbBKcOz+fllDYgTR1psfGapFs9Fmmc7nt9boB+U6Z8bIoNHnnVn6Tit5pOMv+CMrpMTTVMMQgPAgMBAAECgYA4lnpOsbSREeAbfEp8ynaY/Jk9r/ep11irdAkSXYL6/IUwMEVQte9OGy0s+uECLFTd+GYQNVhsbfPE29ltqnMD6VdyITrGstg5kD8o4BbkBUK7jsjrzZc+E0VY88+JB5iRFyQGJrrc6dj3zqtMwF2wIKW38NUZjueNs7zc9U0TmQJBAO/8PfgG+ieK3sA9NH48VYh7B/7Z4U23VjZRNE3vU1VlMEJj9FBSG5j/TMkhJoITLzzeZUWfhBNFpxFUVg0fe20CQQDhiHrOCYx/dT7nXfyb/h+qCySfRqA0ZQi5uZY2yhRSZoFlBw4lcFzkIQSWGJEq6Vh4AoAsArq6o9xhZBJ8HcfrAkBAgbzmfnmfR2S4p7sEc5NKLEfp6Qb2rOEvmIRGrp3nxltCfTuem3NvnLA3IAIJ1L5NzjnKjSYjSAWwUssOm/fBAkA0gCjWPbLPGOQDyAU96vJEC0QDL9WrXLmjQmiNaI1CgUot0m6gmH+HO4htyFpCuv81MJ/Vnu0I9Ywfv8Pt0KDxAkEA3ZKFsn7rILTetAntcr9bjr9vHzCtIT5qdATuEuK7R1e3NQz/R/IsXI8sfYyyc8do+wqu+7ywsB5eoN+W2+lZvw==";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    @InjectExtra(Constants.Extra.ORDER)
    public Order order;

//    @InjectView(R.id.btn_pay)
//    Button btn_pay;
    private AlertDialog alert_dialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
//                    Toast.makeText(PayActivity.this, payResult.getResult(),
//                            Toast.LENGTH_LONG).show();

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
//                        alert_dialog.dismiss();
                        finish_with_result();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay);
        setTitle("选择支付");

        System.out.println("pay");
//        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(PayActivity.this)
//                .setTitle("支付")
//                .setMessage("请根据支付情况选择相应选项")
//                .setNegativeButton("支付成功", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish_with_result();
//                    }
//                })
//                .setNeutralButton("遇到问题？", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        repay();
//                    }
//                });
//        alert_dialog = dialog_builder.create();
//        alert_dialog.show();
        alipay();
    }

    private void alipay() {
        String str_total = String.format("%.2f", order.get_total());
        System.out.println("str_total:" + str_total);
        final String orderInfo = getOrderInfo("订单" + order.get_id(), "订单" + order.get_id() , str_total);
//        final String orderInfo = order.get_id(); // 订单信息

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
//                String result = alipay.pay(orderInfo);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
// 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    private void finish_with_result() {
        System.out.println("finish_with_result");
        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra(Constants.Extra.ORDER, order);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void repay() {
        // todo only dismiss? or goto wiki
        System.out.println("repay");
        alert_dialog.dismiss();
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.get_id() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://yishipa.com/orders/alipay_app_notify"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
