package com.example.ecrbtb.mvp.goods.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.bean.PriceRules;
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
 * Created by boby on 2016/12/19.
 */

public class GoodsBiz extends BaseBiz {

    private static Context mContext;

    public GoodsBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static GoodsBiz INSTANCE = new GoodsBiz(mContext);
    }

    public static GoodsBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return GoodsBiz.SingletonHolder.INSTANCE;
    }

    /**
     * 请求货品数据
     *
     * @param hm
     */
    public void questGoodsData(HashMap<String, String> hm, final IGoodsBiz iGoodsBiz) {

        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.GOODS_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        iGoodsBiz.responseCode(Constants.REQUEST_SUCCESS_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iGoodsBiz.responseCode(Constants.REQUEST_FAILED_CODE);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                saveGoodsData(resultJson);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    //保存goods数据
    private void saveGoodsData(String resultJson) {
        if (resultJson.contains("error_response"))
            return;
        try {
            JSONArray jsonArray = new JSONArray(resultJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    //JSONArray specArray = jsonObject.optJSONArray("Specifiction");
                    JSONArray goodsArray = jsonObject.optJSONArray("Goods");
                    for (int j = 0; j < goodsArray.length(); j++) {
                        JSONObject goodsObject = goodsArray.optJSONObject(j);
                        if (goodsObject.has("Id")) {
                            Goods goods = mGson.fromJson(goodsObject.toString(), Goods.class);
                            if (findGoodsById(goods.Id, goods.SupplierId) == null) {
                                db.save(goods);
                            }
                        }

                        if (goodsObject.has("PriceRules")) {
                            JSONArray priceRulesArray = goodsObject.optJSONArray("PriceRules");
                            for (int k = 0; k < priceRulesArray.length(); k++) {
                                JSONObject priceObject = priceRulesArray.getJSONObject(k);

                                //保存货品的价格体系
                                PriceRules priceRules = findPriceRulesById(priceObject.optInt("Id"));
                                if (priceRules == null) {
                                    PriceRules pr = mGson.fromJson(priceObject.toString(), PriceRules.class);
                                    pr.GoodsId = goodsObject.optInt("Id");
                                    pr.ProductId = goodsObject.optInt("ProductId");
                                    pr.Unit = goodsObject.optString("Unit");
                                    pr.SupplierId = goodsObject.optInt("SupplierId");
                                    db.save(pr);
                                }
                            }
                        }

//                        if (goodsObject.has("Specs")) {
//                            JSONArray specsArray = goodsObject.optJSONArray("Specs");
//                            for (int n = 0; n < specsArray.length(); n++) {
//                                JSONObject specObject = specsArray.getJSONObject(n);
//                            }
//                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param productId
     * @return
     */
    public List<Goods> getGoodsData(int productId, int supplierId) {
        List<Goods> goodsList = new ArrayList<>();
        try {
            goodsList = db.selector(Goods.class)
                    .where("ProductId", "=", productId)
                    .and("supplierId", "=", supplierId)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return goodsList;
    }


    /**
     * 获取价格体系数据
     *
     * @param supplierId
     * @param productId
     * @param goodsId
     * @return
     */
    public List<PriceRules> getPriceRulesList(int supplierId, int productId, int goodsId) {
        List<PriceRules> rulesList = new ArrayList<>();
        try {
            rulesList = db.selector(PriceRules.class)
                    .where("SupplierId", "=", supplierId)
                    .and("ProductId", "=", productId)
                    .and("GoodsId", "=", goodsId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return rulesList;
    }


    //查找good数据是否存在
    public Goods findGoodsById(int goodsId, int supplierId) {
        try {
            Goods goods = db.selector(Goods.class)
                    .where("GoodsId", "=", goodsId)
                    .and("SupplierId", "=", supplierId)
                    .findFirst();
            return goods;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找价格体系
     *
     * @return
     */
    public PriceRules findPriceRulesById(int priceRulesId) {
        try {
            PriceRules pr = db.selector(PriceRules.class).where("PriceRulesId", "=", priceRulesId).findFirst();
            return pr;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param productId
     * @param supplierId
     * @param num
     */
    public void saveProductNum(int productId, int supplierId, int num) {
        CategoryBiz.getInstance(mContext).updateProductNumById(productId, supplierId, num);
    }

    /**
     * @param goodsId
     * @param num
     */
    public void updateGoodsNumById(int goodsId, int supplierId, int num) {
        try {
            Goods goods = findGoodsById(goodsId, supplierId);
            if (goods != null) {

                String[] columns = null;
                if (goods.GoodsNumber == 0 && num != 0) {
                    goods.AddCartTime = System.currentTimeMillis();
                    columns = new String[]{"GoodsNumber", "AddCartTime"};
                } else if (num == 0) {
                    goods.AddCartTime = 0;
                    columns = new String[]{"GoodsNumber", "AddCartTime"};
                } else {
                    columns = new String[]{"GoodsNumber"};
                }

                goods.GoodsNumber = num;
                db.update(goods, columns);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param isDeduction
     * @param goods
     * @return
     */
    public String getGoodsPriceData(String isDeduction, Goods goods) {
        String result = "";
        switch (isDeduction) {
            case "1":
                result = getRulesByDeduction(goods.SalesPrice, goods.SalesIntegral);
                break;
            case "0":
            default:
                result = getRulesBySaleMode(goods.SaleMode, goods.SalesPrice, goods.SalesIntegral);
                break;
        }
        return result;
    }

    /**
     * 转换类型获取价格数据
     *
     * @param isDeduction
     * @param product
     * @return
     */
    public String getGoodsPriceData(String isDeduction, Product product) {
        String result = "";
        switch (isDeduction) {
            case "1":
                result = getRulesByDeduction(product.SalesPrice, product.SalesIntegral);
                break;
            case "0":
            default:
                result = getRulesBySaleMode(product.SaleMode, product.SalesPrice, product.SalesIntegral);
                break;
        }
        return result;
    }


    /**
     * @param isDeduction
     * @param price
     * @param integral
     * @return
     */
    public String getPriceRulesData(String isDeduction, int saleMode, double price, double integral) {
        String result = "";
        switch (isDeduction) {
            case "1":
                result = getRulesByDeduction(price, integral);
                break;
            case "0":
            default:
                result = getRulesBySaleMode(saleMode, price, integral);
                break;
        }
        return result;
    }


    /**
     * 获取价格体系 字符串数据
     *
     * @param isDeduction
     * @param item
     * @return
     */
    public String[] getPriceRulesData(String isDeduction, int saleMode, Goods item) {
        String[] rules = new String[2];
        List<PriceRules> rulesList = getPriceRulesList(item.SupplierId, item.ProductId, item.Id);
        if (rulesList == null || rulesList.isEmpty()) {
            rules[0] = "";
            rules[1] = getGoodsPriceData(isDeduction, item);
        } else {
            StringBuffer sb = new StringBuffer();
            int index = 0;
            for (PriceRules pr : rulesList) {
                index++;

                String priceData = getPriceRulesData(isDeduction, saleMode, pr.Price, pr.Integral);
                if (pr.MaxQuantity >= 2.147483647E9) {
                    sb.append("≥" + pr.MinQuantity + pr.Unit + " " + priceData);
                } else {
                    sb.append(pr.MinQuantity + "-" + pr.MaxQuantity + pr.Unit + " " + priceData);
                }

                sb.append("    ");

                if (rulesList.size() > index && index % 2 == 0) {
                    sb.append("\n");
                }

                if (item.GoodsNumber >= pr.MinQuantity && item.GoodsNumber <= pr.MaxQuantity) {
                    rules[1] = priceData;
                } else if (item.GoodsNumber >= pr.MaxQuantity) {
                    rules[1] = priceData;
                }

//                if (item.GoodsNumber >= pr.MinQuantity) {
//                    item.ActualUnitPrice = pr.Price;
//                }
            }

            //处理数量小于 MinQuantity 的情况
            if (item.GoodsNumber < rulesList.get(0).MinQuantity) {
                rules[1] = getGoodsPriceData(isDeduction, item);
            }

            rules[0] = sb.toString();
        }
        return rules;
    }


    /**
     * 转换类型获取价格体系
     *
     * @param isDeduction
     * @param saleMode
     * @param item
     * @return
     */
    public String[] getPriceRulesData(String isDeduction, int saleMode, Product item) {
        Goods goods = new Goods();
        goods.SupplierId = item.SupplierId;
        goods.DefaultPic = item.DefaultPic;
        goods.GoodsNumber = item.ProductNum;
        goods.Id = item.GoodsId;
        goods.AddCartTime = item.AddCartTime;
        goods.ProductName = item.ProductName;
        goods.ProductId = item.ProductId;
        goods.SaleMode = item.SaleMode;
        goods.SalesPrice = item.SalesPrice;
        goods.SalesIntegral = item.SalesIntegral;

        return getPriceRulesData(isDeduction, saleMode, goods);

    }


}
