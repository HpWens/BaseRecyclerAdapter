package com.example.ecrbtb.mvp.quick_order.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.mvp.quick_order.bean.Order;
import com.example.ecrbtb.mvp.quick_order.bean.OrderItem;
import com.example.ecrbtb.mvp.quick_order.bean.Purchase;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by boby on 2017/1/10.
 */

public class CollectionBiz extends BaseBiz {

    public CollectionBiz(Context context) {
        super(context);
    }

    private static Context mContext;

    private static class SingletonHolder {
        private static CollectionBiz INSTANCE = new CollectionBiz(mContext);
    }

    public static CollectionBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return SingletonHolder.INSTANCE;
    }


    //请求收藏数据
    public void requestCollectionData(int pageState, HashMap<String, String> hm, final IComResponse<Product> response) {
        String url = pageState == 0 ? Constants.COLLECTION_PRODUCT_URL : Constants.COMMODITY_PRODUCT_URL;
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(url, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                response.getResponseData(handlerCollectionData(resultJson));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * @param json
     */
    public List<Product> handlerCollectionData(String json) {
        List<Product> productList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int count = jsonObject.optInt("count");
            if (count == 0) {
                return new ArrayList<>();
            }
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject collectionObject = jsonArray.optJSONObject(i);
                Product pt = findProductById(collectionObject.optInt("ProductId"), collectionObject.optInt("SupplierId"));
                if (pt == null) {
                    Product product = mGson.fromJson(jsonArray.optJSONObject(i).toString(), Product.class);
                    db.save(product);
                    productList.add(product);
                } else {
                    productList.add(pt);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return productList;
    }


    /**
     * 查找商品是否存在
     *
     * @param productId  产品ID
     * @param supplierId 商家ID
     * @return
     */
    public Product findProductById(int productId, int supplierId) {
        try {
            Product product = db.selector(Product.class)
                    .where("ProductId", "=", productId)
                    .and("SupplierId", "=", supplierId)
                    .findFirst();
            return product;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * //请求常规商品数据
     *
     * @param hm
     * @param response
     */
    public void requestCommodityData(HashMap<String, String> hm, final IComResponse<Product> response) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.COMMODITY_PRODUCT_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                response.getResponseData(handlerCollectionData(resultJson));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * //请求常规商品数据
     *
     * @param hm
     * @param response
     */
    public void requestPurchaseData(HashMap<String, String> hm, final IComResponse<Purchase> response) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.PURCHASE_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                response.getResponseData(handlerPurchaseData(resultJson));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 处理长购订单数据
     *
     * @param json
     */
    public List<Purchase> handlerPurchaseData(String json) {
        List<Order> orderList = new ArrayList<>();
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Purchase> purchaseList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optInt("count") != 0) {

                JSONArray dataArray = jsonObject.optJSONArray("data");

                for (int k = 0; k < dataArray.length(); k++) {

                    JSONObject dataObject = dataArray.optJSONObject(k);

                    JSONArray orderArray = dataObject.optJSONArray("Order");

                    for (int j = 0; j < orderArray.length(); j++) {
                        JSONObject orderObject = orderArray.optJSONObject(j);

                        Order order = mGson.fromJson(orderObject.toString(), Order.class);

                        orderList.add(order);
                    }

                    JSONArray orderItemArray = dataObject.optJSONArray("OrderItem");
                    for (int i = 0; i < orderItemArray.length(); i++) {
                        JSONObject orderItemObject = orderItemArray.optJSONObject(i);

                        OrderItem orderItem = mGson.fromJson(orderItemObject.toString(), OrderItem.class);

                        orderItemList.add(orderItem);
                    }

                    for (Order order : orderList) {
                        Purchase header = new Purchase();
                        header.itemType = Purchase.HEADER;
                        header.OddNumber = order.OddNumber;
                        header.SupplierName = order.fkName;
                        purchaseList.add(header);
                        for (OrderItem orderItem : orderItemList) {
                            if (order.Id == orderItem.OrderId) {
                                Purchase content = new Purchase();
                                content.itemType = Purchase.CONTEXT;
                                content.DefaultPic = orderItem.DefaultPic;
                                content.Name = orderItem.Name;
                                content.SpecValue = orderItem.SpecValue;
                                content.Quantity = orderItem.Quantity;
                                content.Price = "¥" + orderItem.Price;
                                content.SupplierId = order.FKId;
                                content.OddNumber = order.OddNumber;
                                content.OrderId = orderItem.OrderId;
                                purchaseList.add(content);
                            }
                        }
                        Purchase footer = new Purchase();
                        footer.itemType = Purchase.FOOTER;
                        footer.ItemCount = order.ItemCount;
                        footer.Id = order.Id;
                        if (order.Payables != 0 && order.PayableIntegral == 0) {
                            footer.Price = "¥" + order.Payables;
                        } else if (order.Payables == 0 && order.PayableIntegral != 0) {
                            footer.Price = "积分" + order.PayableIntegral;
                        } else {
                            footer.Price = "¥" + order.Payables + "+积分" + order.PayableIntegral;
                        }
                        purchaseList.add(footer);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return purchaseList;
    }


    /**
     * @param purchaseList
     */
    public void payAgain(List<Purchase> purchaseList) {

        for (Purchase purchase : purchaseList) {
            Goods goods = new Goods();



        }

    }

}
