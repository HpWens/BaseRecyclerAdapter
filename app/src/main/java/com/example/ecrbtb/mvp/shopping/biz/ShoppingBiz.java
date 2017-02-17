package com.example.ecrbtb.mvp.shopping.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.StringUtils;

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
 * Created by boby on 2016/12/22.
 */

public class ShoppingBiz extends BaseBiz {

    public ShoppingBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static ShoppingBiz INSTANCE = new ShoppingBiz(mContext);
    }

    public static ShoppingBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return ShoppingBiz.SingletonHolder.INSTANCE;
    }


    /**
     * 获取购物车的数据
     */
    public List<Product> getShoppingCartData() {
        List<Product> lists = null;
        try {
            lists = db.selector(Product.class).where("ProductNum", ">", 0).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * @return
     */
    public List<Product> getProductListData() {
        try {
            return db.selector(Product.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param goodsList
     * @return
     */
    public List<Product> transformerProductList(List<Goods> goodsList) {
        List<Product> productList = new ArrayList<>();
        if (goodsList == null || goodsList.isEmpty())
            return productList;
        for (Goods goods : goodsList) {
            Product product = new Product();

            product.ProductId = goods.ProductId;
            product.SupplierId = goods.SupplierId;


        }
        return productList;
    }


    //获取货品num 大于0的集合
    public List<Goods> getGoodsByNum(int supplierId, int productId) {
        try {
            return db.selector(Goods.class)
                    .where("SupplierId", "=", supplierId)
                    .and("ProductId", "=", productId)
                    .and("GoodsNumber", ">", 0)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把商品从购物车删除
     *
     * @param supplierId
     * @param productId
     */
    public void deleteProduct(int supplierId, int productId) {
        //注意这里的数量不应该为0
        deleteProduct(supplierId, productId, 0);
    }


    /**
     * @param supplierId
     * @param productId
     * @param productNum
     */
    public void deleteProduct(int supplierId, int productId, int productNum) {
        try {
            Product product = db.selector(Product.class)
                    .where("SupplierId", "=", supplierId)
                    .and("ProductId", "=", productId)
                    .findFirst();
            if (product != null) {
                product.ProductNum -= productNum;
                product.AddCartTime = 0;
                db.update(product, new String[]{"ProductNum", "AddCartTime"});
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除货品数据
     *
     * @param supplierId
     * @param goodsId
     */
    public void deleteGoods(int supplierId, int goodsId, int goodsNum) {
        try {
            Goods goods = db.selector(Goods.class)
                    .where("SupplierId", "=", supplierId)
                    .and("GoodsId", "=", goodsId)
                    .findFirst();
            if (goods != null) {
                goods.GoodsNumber -= goodsNum;
                goods.AddCartTime = 0;
                db.update(goods, new String[]{"GoodsNumber", "AddCartTime"});
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交结算数据
     *
     * @param hm
     */
    public void commitSettlement(HashMap<String, String> hm, final MyResponseListener listener) {
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
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonStr);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    /**
     * 获取结算数据
     *
     * @param productList
     * @param buyType
     * @return
     */
    public String getSettlementData(List<Product> productList, String buyType) {
        StringBuilder sb = new StringBuilder();
        if (productList == null || productList.isEmpty()) {
            return "";
        }
        for (Product product : productList) {
            if (product.ProductNum == 0)
                continue;
            sb.append("<Goods>");
            sb.append("<SupplierId>" + product.SupplierId + "</SupplierId>");
            sb.append("<BuyTypeId>0</BuyTypeId>");
            sb.append("<BuyType>" + buyType + "</BuyType>");
            sb.append("<productid>" + product.ProductId + "</productid>");
            sb.append("<goodsid>" + product.GoodsId + "</goodsid>");
            sb.append("<specvalue></specvalue>");
            sb.append("<quantity>" + product.ProductNum + "</quantity>");
            sb.append("<depotid>0</depotid>");
            sb.append("</Goods>");
        }
        return sb.toString();
    }


}
