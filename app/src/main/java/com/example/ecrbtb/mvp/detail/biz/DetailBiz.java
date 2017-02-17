package com.example.ecrbtb.mvp.detail.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by boby on 2016/12/27.
 */

public class DetailBiz extends BaseBiz {

    private static Context mContext;


    public DetailBiz(Context context) {
        super(context);
    }


    private static class SingletonHolder {
        private static DetailBiz INSTANCE = new DetailBiz(mContext);
    }

    public static DetailBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return DetailBiz.SingletonHolder.INSTANCE;
    }


    /**
     * @param hm
     * @param productId
     */
    public void requestDetailData(HashMap<String, String> hm, final int productId, final int supplierId, final IResponse iResponse) {

        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.DETAIL_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        iResponse.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iResponse.onFailed();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            if (!StringUtils.isEmpty(jsonStr)) {
                                updateProductData(jsonStr, productId, supplierId, iResponse);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //更新商品数据
    private void updateProductData(String jsonStr, int productId, int supplierId, IResponse iResponse) {
        try {
            //{"error_response":{"code":"0003","msg":"在位置 0 处没有任何行。"}}
            if (jsonStr.contains("error_response")) {
                return;
            }

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (!jsonObject.has("main")) {
                    return;
                }
                JSONArray mainArray = jsonObject.optJSONArray("main");
                for (int j = 0; j < mainArray.length(); j++) {
                    JSONObject mainObject = mainArray.optJSONObject(j);
                    //更新库存
                    //判断改商品是否存在数据库
                    if (findProductById(productId, supplierId) == null) {
                        Product product = mGson.fromJson(mainObject.toString(), Product.class);
                        product.ProductId = productId;
                        product.SupplierId = supplierId;
                        try {
                            db.save(product);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    updateStockAndCategoryName(mainObject.optInt("pid"), supplierId, mainObject.optDouble("stock"), mainObject.optString("categoryname"));
                    //回调图文参数
                    String intro = mainObject.optString("intro");
                    StringBuilder sb = new StringBuilder();
                    if (!StringUtils.isEmpty(intro)) {
                        if (intro.contains("src=")) {
                            while (true) {
                                intro = intro.substring(intro.indexOf("src=") + 5);
                                if (intro == null || StringUtils.isEmpty(intro)) return;
                                if (!intro.startsWith("http://")) {
                                    break;
                                } else {
                                    String url = intro.substring(0, intro.indexOf("\""));
                                    sb.append(url + ",");
                                }
                            }
                        }
                    }
                    if (iResponse != null) {
                        iResponse.getPhoneUrls(sb.toString());
                    }
                }

                JSONArray picArray = jsonObject.optJSONArray("pics");
                JSONArray pArray = picArray.optJSONArray(0);

                String pics = "";
                for (int k = 0; k < pArray.length(); k++) {
                    //更新图片
                    pics += pArray.optJSONObject(k).optString("pic") + ",";
                }

                if (!StringUtils.isEmpty(pics)) {
                    pics = pics.substring(0, pics.length() - 1);
                    updatePic(productId, supplierId, pics);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新库存
     *
     * @param productId
     * @param stock
     */
    public void updateStockAndCategoryName(
            int productId, int supplierId, double stock, String categoryName) {
        try {
            Product product = findProductById(productId, supplierId);
            if (product != null) {
                product.Stock = stock;
                product.CategoryName = categoryName;
                //product.BrandName = brandName;
                db.update(product, new String[]{"Stock", "CategoryName"});
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param productId
     * @param pics
     */
    public void updatePic(int productId, int supplierId, String pics) {
        Product product = findProductById(productId, supplierId);
        try {
            if (product != null) {
                product.Pics = pics;
                db.update(product, new String[]{"Pics"});
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param productId
     * @return
     */
    public Product findProductById(int productId, int supplierId) {
        Product product = null;
        try {
            product = db.selector(Product.class).where("ProductId", "=", productId)
                    .and("SupplierId", "=", supplierId)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * @return
     */
    public int getStoreId() {
        return prefer.getInt(Constants.STORE_ID, 0);
    }

    /**
     * @param hm
     */
    public void requestLikeData(HashMap<String, String> hm, final int supplierId, final ILikeResponse iLikeResponse) {

        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.LIKE_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            if (!StringUtils.isEmpty(jsonStr)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonStr);
                                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                                    List<Product> productList = new ArrayList<Product>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Product product = mGson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                                        product.SupplierId = supplierId;
                                        productList.add(product);
                                    }

                                    if (iLikeResponse != null) {
                                        iLikeResponse.getLikeData(productList);
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
     * @param productId
     * @param supplierId
     * @param productNum
     */
    public void updateSingleProductGoodsNum(int productId, int supplierId, int productNum) {

        try {
            Goods goods = db.selector(Goods.class).where("SupplierId", "=", supplierId)
                    .and("ProductId", "=", productId).findFirst();
            if (goods != null) {
                goods.GoodsNumber = productNum;
                db.update(goods, new String[]{"GoodsNumber"});
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交订单数据
     *
     * @param hm
     */
    public void commitOrderData(final HashMap<String, String> hm, final MyResponseListener listener) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.COMMIT_ORDER_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            if (!StringUtils.isEmpty(jsonStr)) {
                                handlerCommitOrderData(jsonStr, listener);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 处理订单数据
     *
     * @param json
     * @param listener
     */
    public void handlerCommitOrderData(String json, MyResponseListener listener) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("error_response")) {
                JSONObject errorObject = jsonObject.optJSONObject("error_response");
                if (listener != null)
                    listener.onError("" + errorObject.optString("msg"));
            } else {
                if (listener != null)
                    listener.onResponse("" + jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param url
     */
    public int[] getImageUrlPixels(String url) {
        int[] pixels = new int[2];
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            pixels[0] = bitmap.getWidth();
            pixels[1] = bitmap.getHeight();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pixels;
    }


    /**
     * @param hm
     */
    public void requestCollection(HashMap<String, String> hm, String requestUrl, final IComResponse iComResponse) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(requestUrl, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iComResponse.onError(e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            if (!StringUtils.isEmpty(jsonStr)) {
                                iComResponse.onComplete(jsonStr);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
