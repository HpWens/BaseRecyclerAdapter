package com.example.ecrbtb.mvp.order.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.order.bean.Address;
import com.example.ecrbtb.mvp.order.bean.Coupon;
import com.example.ecrbtb.mvp.order.bean.OrderData;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

import static android.R.attr.type;

/**
 * Created by boby on 2017/1/6.
 */

public class OrderBiz extends BaseBiz {

    private static Context mContext;

    public OrderBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static OrderBiz INSTANCE = new OrderBiz(mContext);
    }

    public static OrderBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return SingletonHolder.INSTANCE;
    }

    /**
     * 处理订单数据
     *
     * @param json
     */
    public void handlerOrderData(String json, IOrderResponse iResponse) {
        if (StringUtils.isEmpty(json)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            int addressId = jsonObject.optInt("AddressId");
            if (addressId != 0) {
                getDefaultAddress(addressId);
            }

            //获取总金额、积分
            double price = jsonObject.optDouble("Payables");
            int integral = jsonObject.optInt("PayableIntegral");

            String productIds = jsonObject.optString("ProductIds");
            if (productIds.contains("[") && productIds.contains("]")) {
                productIds = productIds.replace("[", "").replaceAll("]", "");
            }
            String productInfo = jsonObject.optString("ProductInfo");

            iResponse.getPriceAndIntegral(price, integral);
            iResponse.getCommitData(productIds, productInfo);

            if (jsonObject.has("Suppliers")) {
                getOrderListData(jsonObject.optString("Suppliers"), iResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param json
     */
    private void getOrderListData(String json, IOrderResponse iResponse) {
        List<Goods> goodsList = new ArrayList<>();
        List<Integer> supplierList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);

                int supplierId = jsonObject.optInt("SupplierId");
                String supplierName = jsonObject.optString("Name");

                supplierList.add(supplierId);

                JSONArray productArray = jsonObject.optJSONArray("Products");

                for (int j = 0; j < productArray.length(); j++) {

                    JSONObject productObject = productArray.optJSONObject(j);

                    JSONArray goodsArray = productObject.optJSONArray("Goods");

                    for (int k = 0; k < goodsArray.length(); k++) {

                        JSONObject goodsObject = goodsArray.optJSONObject(k);

                        Goods goods = new Goods();
                        goods = mGson.fromJson(goodsObject.toString(), Goods.class);
                        goods.SupplierId = supplierId;
                        goods.SupplierName = supplierName;
                        goods.GoodsNumber = goodsObject.optInt("Quantity");

                        goodsList.add(goods);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        iResponse.getResponseData(goodsList);
        iResponse.getSuppliers(supplierList);
    }

    /**
     * @param addressId
     */
    public void getDefaultAddress(int addressId) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("Id", addressId + "");
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.GOODS_RECEIPT_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 获取地址列表数据
     */
    public void getAddressListData(final IComResponse<Address> listener) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("StoreId", getStoreId() + "");
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.ADDRESS_LIST_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e.toString());
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                handlerAddressListData(resultJson, listener);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 处理地址列表数据
     *
     * @param json
     */
    public void handlerAddressListData(String json, IComResponse<Address> listener) {
        JSONArray jsonArray = null;
        List<Address> addresses = new ArrayList<>();
        try {
            jsonArray = new JSONArray(json);
            if (jsonArray.isNull(0)) {
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject addressObject = jsonArray.optJSONObject(i);

                Address address = mGson.fromJson(addressObject.toString(), Address.class);

                addresses.add(address);
            }

            if (listener != null) {
                listener.getResponseData(addresses);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交新增地址数据
     *
     * @param address
     */
    public void commitAddAddressData(Address address, final MyResponseListener listener) {

        HashMap<String, String> hm = new HashMap<>();
        hm.put("FK_Id", getStoreId() + "");
        hm.put("Name", address.Name + "");
        hm.put("Mobile", address.Mobile + "");
        hm.put("Province", address.Province + "");
        hm.put("City", address.City + "");
        hm.put("Area", address.Area + "");
        hm.put("Address", address.Address + "");

        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.ADD_ADDRESS_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError("" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {

                                if (resultJson.equals("false")) {
                                    listener.onError("新增地址失败");
                                } else {
                                    try {
                                        JSONArray jsonArray = new JSONArray(resultJson);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject object = jsonArray.optJSONObject(i);

                                            int resultCode = object.optInt("Result");

                                            if (resultCode != 0) {
                                                listener.onResponse(object.optString("新增收货地址成功"));
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 请求运费接口
     *
     * @param hm
     */
    public void requestFreightData(HashMap<String, String> hm, final IComResponse response) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.FREIGHT_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                response.onComplete(resultJson);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 提交订单数据
     *
     * @param hm
     */
    public void commitOrderData(HashMap<String, String> hm, final int payType, final IComResponse response) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.ORDER_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //服务器错误
                        response.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                try {
                                    JSONObject resultObject = new JSONObject(resultJson);
                                    if (resultObject.has("error_response")) {//提交订单失败
                                        String error = resultObject.optJSONObject("error_response").optString("msg");
                                        response.onError(error);
                                    } else {//提交订单成功
                                        String url = "";
                                        if (payType == 2) {
                                            url = Constants.PAY_BASE_URL + "/Pay/Cashier.aspx?" + "Data=" + resultObject.optString("Data")
                                                    + "&Sign=" + resultObject.optString("Sign");
                                        } else if (payType == 3) {
                                            url = Constants.PAY_BASE_URL + Constants.ACCOUNT_PAY + resultObject.optString("Data");
                                        } else { //0 线下支付
                                            url = Constants.PAY_BASE_URL + "/Trade/Supplier/OrderSuccess.aspx?OddNumber=" +
                                                    StringUtils.getBase64String(resultObject.optString("OddNumber"));
                                        }
                                        response.onComplete(url);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * @return String productIds, String productInfo, Address address, int payType,
     * double payPrice, int invoiceType, List<Coupon> couponList,
     * int type, String rise, String content, String[] incrementInvoices
     */
    public String composeOrderData(OrderData orderData) {
        StringBuilder sb = new StringBuilder();
        sb.append("<root>");
        sb.append("<action></action>");

        sb.append("<Buy>");
        //逗号隔开 字符串
        sb.append("<ProductIds>" + orderData.ProductIds + "</ProductIds>");
        sb.append("<ProductInfo>" + orderData.ProductInfo + "</ProductInfo>");
        sb.append("<AddressId>" + orderData.AddressData.Id + "</AddressId>");
        sb.append("<AddressProvince>" + orderData.AddressData.Province + "</AddressProvince>");
        //线上 2  线下1  账期3
        sb.append("<PayTypeId>" + orderData.PayType + "</PayTypeId>");
        //金额
        sb.append("<Payables>" + orderData.PayPrice + "</Payables>");

        //抵扣金额
        sb.append("<AccountDeductionIntegral>" + 0 + "</AccountDeductionIntegral>");

        //不需要 0  普通1  增值2
        sb.append("<InvoiceType>" + orderData.InvoiceType + "</InvoiceType>");

        sb.append("</Buy>");


        /******************************/
        sb.append("<Supplier>");

        for (Coupon coupon : orderData.Coupons) {
            //循环
            sb.append("<SupplierId>" + coupon.SupplierId + "</SupplierId>");
            sb.append("<BuyRemark>" + coupon.BuyRemark + "</BuyRemark>");

            sb.append("<Coupon>");
            sb.append("<CouponId>" + coupon.CouponId + "</CouponId>");
            sb.append("<CouponNumber>" + coupon.CouponNumber + "</CouponNumber>");
            sb.append("<CouponPassword>" + coupon.CouponPassword + "</CouponPassword>");
            sb.append("</Coupon>");
        }

        sb.append("</Supplier>");


        /******************************/
        sb.append("<Consignee>");

        sb.append("<Province>" + orderData.AddressData.Province + "</Province>");
        sb.append("<City>" + orderData.AddressData.City + "</City>");
        sb.append("<Area>" + orderData.AddressData.Area + "</Area>");
        sb.append("<Address>" + orderData.AddressData.Address + "</Address>");
        sb.append("<Name>" + orderData.AddressData.Name + "</Name>");
        sb.append("<Mobile>" + orderData.AddressData.Mobile + "</Mobile>");

        sb.append("</Consignee>");


        /******************************/
        sb.append("<Invoice>");
        //0   1   2
        sb.append("<InvType>" + orderData.InvoiceType + "</InvType>");
        if (orderData.InvoiceType == 1) {
            // 个人0  单位1
            sb.append("<TaxpayerType>" + type + "</TaxpayerType>");
            sb.append("<InvTitle>" + orderData.CommInvoiceRise + "</InvTitle>");
            //传中文   明细/办公/配件
            sb.append("<Content>" + orderData.CommInvoiceContent + "</Content>");
        } else if (orderData.InvoiceType == 2) {
            sb.append("<Name>" + orderData.IncrementInvoices[0] + "</Name>");
            sb.append("<Code>" + orderData.IncrementInvoices[1] + "</Code>");
            sb.append("<Address>" + orderData.IncrementInvoices[2] + "</Address>");
            sb.append("<Tel>" + orderData.IncrementInvoices[3] + "</Tel>");
            sb.append("<Bank>" + orderData.IncrementInvoices[4] + "</Bank>");
            sb.append("<Account>" + orderData.IncrementInvoices[5] + "</Account>");
            sb.append("<PostAddress>" + orderData.IncrementInvoices[6] + "</PostAddress>");
        }
        sb.append("</Invoice>");

        sb.append("</root>");
        return sb.toString();
    }

}
