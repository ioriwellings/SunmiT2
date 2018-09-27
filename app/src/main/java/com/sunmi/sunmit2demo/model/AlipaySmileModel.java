package com.sunmi.sunmit2demo.model;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sunmi.sunmit2demo.bean.BizContentBean;
import com.sunmi.sunmit2demo.bean.MenuBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openapi.AlipayCallBack;
import openapi.AlipayClient;
import openapi.AlipayResponse;
import openapi.DefaultAlipayClient;
import openapi.ZimIdRequester;
import openapi.request.AlipayTradePayRequest;
import openapi.response.AlipayTradePayResponse;


/**
 * Created by zhicheng.liu on 2018/4/2
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class AlipaySmileModel {
    public static String merchantId = "2088231586532508";
    public static String appKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3T7ExqgKeecu7tp9zPlTY190R1moj9cp970RFh6h1isGObk13t0+Sz1OVuDQcXqOmy4MiglQi+1TCMGfrlsD0kqXqUp70Ob1qQs27eHGXCLxW5kNox9kCHWzRss3eAuZuKQ+sX7z9LHIXrNcoRRC+XNKY6XZnlLhL3DPpb2w/RmT9HPlXoZnRLTKlMxCeUMPklizbbKZd70T6Buv2R0moCKDSq5m9K6oCk68W+zlLmoQmBa5eFZ+oqZwSO0K1ggb/Q/ZuPQeUG4Vz0/G7Ls+BBoD8dP3pLnNSf9kvM8IrI+7hmjs4rehuS7ZVbMyXF3xFw1n2q+t5UoCWdkMbsWOXAgMBAAECggEAD0FUj7uESEf+inqiPmb7jKg6P5fGcYOs/FoDJn4pqM6JWR8Y11OsDZ2vaRBRVeMSX3kkSqTp95dO8HIy08pR+IiTwjJqBA0iFEvG70kQuu8BLYIwLfIjvydPvQvSUh2Pq69DHKdDBRbDz/CUX0eQIyd6ZztWnTwlMjr7Hvwk2AcRlBzZJooKf/4sgdnJEQ6olP+0RIVWWK0OVRm2P1DIFmr1KdyrcRDbjAMN3sZt/ptrdJx2IOgfxuA0z4uCM2fOqJMzVfRbOgB5gGLXJB23ZiGsE9iq6z/NGpUzNpTh6ifJYSLyqzLNrmoa2XsaRpEz4zEFqMg+fu0IMBMJJw/esQKBgQDmkqtjphnAkWnkXCcltiyG/yYQgFa1rZ58OzIlTBAvRZAQiLZl2cqNQ4cnXvk6uz5ynOlioELCsZuiqELFxZ924BPkL52sKfpiEQf/HbvKRmXBs35cthUTbhHgE0FyuIGzVsEJO+ji2PnSpEty7j38k4Obj/sYJ7HrPBhzfYReZQKBgQDLhsYznys88aBcRY81B4JRkKeINXW/NyZOCsZZiF8fTcTnjh6ifJIuQqKMs8TwVwdjrPme8gHE4sc02WL0lguVdG8Q+FLi9GxDK2TM1PvSEt456OSp1xEYz2MX0R9xAbcAQ2S0R80l0ck9jP2AQhRuf5trtJzFFrH9+Q2tWTkMSwKBgQCFfAH3FbEw975bMzKCtZ2/pbUutm38jgADoe2dBBUWmOoXeF5IRrbp4186XtRguQglKExBCdC8kEAvAcRuZMO1+XHql9prUn4fCgccHeWa6/h7FGQiXlODRSTaGau5M7H61k24/9MKiVzQIsd/SQff0rLNe6R9TnXFXqw1KERatQKBgFQua56AcF7lxiFEOCaD85gPD+xbyFcN1Y8kD8SKjmWhQhdQhkVI5yGxOBCgbo3GQW0t55Whna+06RZn7/EgeqwXm7TWMVpkJL9nnGrIkVvjp/0l4iaJqS2s4dzBB5MzVWELfwS+ShEhg24s+dFqA49swnk7HDi82WQl9k1EAT+fAoGBAKKv2nN+IdwVl2MX6l8MzTX9rvPtraMiFZxew+QKXQTZQM91t8xVN+93AQyV9ZYsUC+Ag92+vj6h9otf/T8TkuVLWZjNPUZ/MvaAPVUjBWOj8Rf/S5juNyib1l+Vhl3P4LynW2oFT2RpaHCyGI3wYHJ1FJu4gYoqKAdOHV2BWve2";
    public static String Public_Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpXAL5RftFDXjDrcdjtHoEHFsryXQYjY+iqNf8zbpO5ID8/oXAI8JRa8W73ZgYnaRwRhRO8nasYstoSmwxT22cl+OOJ7qSo2dZFnnYE7tfMK7MVbYz8hoKF83pGijajDlafYZGzu9rJLtOaqo3iV3oWx27bVEoJFm9VgAG6NxBfh2GwvBp90dz/oa9KyGby63bOYOxjSATI0WZWSKgkseP5NunW6VS5ez1DNFM44EApe2Le3D4+Me87bjojYA04xBUjqHFhnkfiUcg90gUaaKxhyiLLCvDVl9J4Pk2zrYW31odYDhp5h9glkA+wDsTrScPd/B5r7jRkRYRpF8Z5LMQIDAQAB";
    public static String appId = "2018090861280840" ;
    public static String payMethod = "zoloz.authentication.customer.smilepay.initialize";
    public static String version = "1.0";
    public static final String url = "https://openapi.alipay.com/gateway.do";
    public static int i = 1;
    private AlipayClient alipayClient;
    private MenuBean menuBean;

    public AlipaySmileModel() {

    }

    private AlipayClient getAlipayClient() {
        if (alipayClient == null)
            alipayClient = new DefaultAlipayClient(url, appId, appKey, "json", "UTF-8", Public_Key, "RSA2");
        return alipayClient;
    }

    public Map<String, String> buildMerchantInfo() {
        Map<String, String> merchantInfo = new HashMap();
        merchantInfo.put("partnerId", merchantId);
        merchantInfo.put("merchantId", merchantId);
        merchantInfo.put("appId", appId);
        merchantInfo.put("merchantKey", "");
        merchantInfo.put("storeCode", "TEST");
        merchantInfo.put("brandCode", "TEST");
        merchantInfo.put("areaCode", "");
        merchantInfo.put("geo", "0.000000,0.000000");
        merchantInfo.put("wifiMac", "");
        merchantInfo.put("wifiName", "");
        merchantInfo.put("deviceNum", merchantId);
        merchantInfo.put("deviceMac", "CC:B8:A8:0F:63:3C");
        return merchantInfo;
    }

    public void setGoods(MenuBean menuBean) {
        this.menuBean = menuBean;
    }


    /**
     * 发送mZimId 给服务器获得mZimInitClientData
     *
     * @param metainfo
     */

    public void getInitClientData(String metainfo, final ZimIdRequester.RequestCallback callback) {
        new ZimIdRequester().request(metainfo, new ZimIdRequester.RequestCallback() {
            @Override
            public void onRequestStart() {
                callback.onRequestStart();
            }

            @Override
            public void onRequestResult(final String zimId, final String zimInitClientData) {
                callback.onRequestResult(zimId, zimInitClientData);
            }
        });

    }

    /**
     * 开始扣款
     *
     * @param fToken
     * @param amount
     * @param out_trade_no
     */
    public void startChargeback(final String out_trade_no, final String fToken, final String amount, final String subject, final String store_id, final AlipayModelCallBack alipayModelCallBack) {
        String money = "0.0" + i;
        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
        String json = JSON.toJSONString(buildAlipayRequest(out_trade_no, fToken, money, subject, store_id));
        Log.e("@@@@@", json);
        request.setBizContent(json);
        getAlipayClient().execute(request, new AlipayCallBack() {
            @Override
            public <T extends AlipayResponse> T onResponse(T response) {
                if (response.isSuccess()) {
                    alipayModelCallBack.onPayResult((AlipayTradePayResponse) response);
                } else {
                    alipayModelCallBack.onFail(response.getSubMsg());
                }
                return response;
            }
        });
    }

    private BizContentBean buildAlipayRequest(String out_trade_no, String fToken, String money, String subject, String store_id) {
        BizContentBean bizContentBean = new BizContentBean();
        bizContentBean.setOut_trade_no(out_trade_no);
        bizContentBean.setScene("security_code");
        bizContentBean.setAuth_code(fToken);
        bizContentBean.setSubject(subject);
        bizContentBean.setProduct_code("FACE_TO_FACE_PAYMENT");
        bizContentBean.setTotal_amount(Double.parseDouble(money));
        bizContentBean.setBody(menuBean.getTitle());
        List<BizContentBean.GoodsDetailBean> goods_detail = new ArrayList<>();
        for (MenuBean.ListBean listBean : menuBean.getList()) {
            BizContentBean.GoodsDetailBean goodsDetailBean = new BizContentBean.GoodsDetailBean();
            goodsDetailBean.setBody(listBean.getParam2());
            goodsDetailBean.setGoods_id(listBean.getParam1());
            goodsDetailBean.setGoods_name(listBean.getParam2());
            goodsDetailBean.setPrice(listBean.getParam3().substring(1));
            goodsDetailBean.setQuantity(1);
            goods_detail.add(goodsDetailBean);
        }
        bizContentBean.setGoods_detail(goods_detail);
        bizContentBean.setOperator_id("yx_001");
        bizContentBean.setStore_id(store_id);
        bizContentBean.setTerminal_id("NJ_T_001");
        bizContentBean.setTimeout_express("2m");
        return bizContentBean;
    }

    public interface AlipayModelCallBack {
        void onPayResult(AlipayTradePayResponse result);

        void onFail(String msg);
    }
}
