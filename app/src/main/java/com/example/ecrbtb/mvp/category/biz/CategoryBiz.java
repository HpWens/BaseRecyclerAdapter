package com.example.ecrbtb.mvp.category.biz;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Category;
import com.example.ecrbtb.mvp.category.bean.Column0;
import com.example.ecrbtb.mvp.category.bean.Column1;
import com.example.ecrbtb.mvp.category.bean.Column2;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.bean.PriceRules;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.SDCardUtils;
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
 * Created by boby on 2016/12/15.
 */

public class CategoryBiz extends BaseBiz implements ICategoryBiz {

    private static Context mContext;

    public CategoryBiz(Context context) {
        super(context);
    }


    private static class SingletonHolder {
        private static CategoryBiz INSTANCE = new CategoryBiz(mContext);
    }

    public static CategoryBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return CategoryBiz.SingletonHolder.INSTANCE;
    }

    /**
     * 请求 category 栏目数据
     */
    @Override
    public void requestCategoryDate(HashMap<String, String> hm, final IResponseListener listener) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.CATEGORY_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
//                        event.code = Constants.REQUEST_SUCCESS_CODE;
//                        event.message = Constants.REQUEST_SUCCESS_MSG;
//                        event.lists = getCategoryData();
//                        EventBus.getDefault().post(event);
                        listener.responseCode(Constants.REQUEST_SUCCESS_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {

//                        event.code = Constants.REQUEST_FAILED_CODE;
//                        event.message = e.toString();
//                        EventBus.getDefault().post(event);
                        listener.responseCode(Constants.REQUEST_FAILED_CODE);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String resultJson = null;
                        try {
                            resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                saveCategoryData(resultJson);
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
    private void saveCategoryData(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.has("Id")) {
                        return;
                    }
                    int categoryId = jsonObject.optInt("Id");
                    if (!findByStoreId(categoryId)) {
                        Category category = mGson.fromJson(jsonObject.toString(), Category.class);
                        db.save(category);
                    }
                    if (jsonObject.optBoolean("isChild")) {
                        saveCategoryData(jsonObject.optString("children"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public List<MultiItemEntity> getCategoryData() {
        ArrayList<MultiItemEntity> datas = new ArrayList<>();
        try {
            List<Category> mLists = db.findAll(Category.class);
            if (mLists != null && !mLists.isEmpty()) {
                Column0 c0 = new Column0();
                c0.id = 0;
                c0.name = "全部";
                c0.isSelected = true;
                Column1 c1 = new Column1();
                Column2 c2 = new Column2();
                for (Category category : mLists) {
                    if (category.Layer == 1) {

                        datas.add(c0);

                        c0 = new Column0();
                        c0.id = category.Id;
                        c0.name = category.CateName;

                    } else if (category.Layer == 2) {

                        c1 = new Column1();
                        c1.id = category.Id;
                        c1.name = category.CateName;

                        c0.addSubItem(c1);

                    }
//                    else if (category.Layer == 3) {
//
//                        c2 = new Column2();
//                        c2.id = category.Id;
//                        c2.name = category.CateName;
//
//                        c1.addSubItem(c2);
//
//                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return datas;
    }


    //id是否已经存在数据库中
    public boolean findByStoreId(int id) {
        try {
            Category category = db.selector(Category.class).where("CategoryId", "=", id).findFirst();
            if (category == null) {
                return false;
            }
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void requestProductData(final HashMap<String, String> hm, final boolean isSearch, final IResponseListener listener) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.PRODUCT_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        listener.responseCode(Constants.REQUEST_SUCCESS_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.responseCode(Constants.REQUEST_FAILED_CODE);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String jsonStr = responseBody.string();
                            if (!StringUtils.isEmpty(jsonStr)) {
                                if (!isSearch) {
                                    saveProductData(jsonStr, Integer.parseInt(hm.get("cid")));
                                } else {
                                    SDCardUtils.saveJsonToFile(mContext, Constants.SEARCH_PRODUCT, jsonStr);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 保存商品数据
     *
     * @param json
     */
    public void saveProductData(String json, int cid) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            if (jsonArray == null || jsonArray.isNull(0)) return;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJson = jsonArray.getJSONObject(i);
                Product product = findProductById(productJson.optInt("ProductId"), productJson.optInt("SupplierId"));
                if (product == null) {
                    Product p = mGson.fromJson(productJson.toString(), Product.class);
                    if (p == null)
                        return;
                    p.CId = cid;
                    db.save(p);
                } else {
                    //当前商品存在,并且判定是否 cid 为-1 , 如果为 -1 , 则更新 CID
                    //if (product.CId == -1) {
                        product.CId = cid;
                        db.update(product, new String[]{"CId"});
                   // }
                }
                //保存单个商品价格体系
                if (productJson.has("PriceRule")) {
                    saveSimplePriceRules(productJson.optJSONArray("PriceRule"), productJson.optString("Unit"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查找单个商品价格体系
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
     * @param supplierId
     * @param productId
     * @return
     */
    public List<PriceRules> findPriceRulesById(int supplierId, int productId) {
        try {
            return db.selector(PriceRules.class)
                    .where("supplierId", "=", supplierId)
                    .and("ProductId", "=", productId)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存单个商品价格体系
     *
     * @param jsonArray
     */
    public void saveSimplePriceRules(JSONArray jsonArray, String unit) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (!jsonObject.has("Id"))
                return;
            PriceRules rules = findPriceRulesById(jsonObject.optInt("Id"));
            if (rules == null) {
                try {
                    PriceRules simpleRules = mGson.fromJson(jsonObject.toString(), PriceRules.class);
                    simpleRules.GoodsId = jsonObject.optInt("Id");
                    simpleRules.ProductId = jsonObject.optInt("ProductId");
                    simpleRules.Unit = unit;
                    simpleRules.SupplierId = jsonObject.optInt("SupplierId");
                    db.save(simpleRules);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
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
     * 页数请求数据
     *
     * @return
     */
    public List<Product> getProductDataByPage(int cid, int pageIndex) {
        List<Product> lists = null;
        try {
            if (cid == 0) {
                lists = db.selector(Product.class)
                        .offset((pageIndex - 1) * 12)
                        .limit(12).findAll();
            } else {
                lists = db.selector(Product.class)
                        .offset((pageIndex - 1) * 12)
                        .limit(12).where("CId", "=", cid).findAll();
            }
            return lists;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判定是否登录
     *
     * @return
     */
    public boolean isLogin() {
        int storeId = prefer.getInt(Constants.STORE_ID, 0);
        if (storeId == 0)
            return false;
        return true;
    }

    /**
     * 是否存在栏目数据
     *
     * @return
     */
    public boolean isExistCategoryData() {
        int type = prefer.getInt(Constants.EXIST_CATEGORY_DATA, Constants.UN_LOAD_CATEGORY);
        return type == 0 ? false : true;
    }

    private void alreadyLoadCategoryData() {
        prefer.edit().putInt(Constants.EXIST_CATEGORY_DATA, Constants.LOADED_CATEGORY).commit();
    }

    //获取得到门店ID
    public int getStoreId() {
        return prefer.getInt(Constants.STORE_ID, 0);
    }


    //获取搜索商品数据  存文件
    public List<Product> getSearchData() {
        List<Product> lists = new ArrayList<>();
        try {
            String json = SDCardUtils.getFileByJson(mContext, Constants.SEARCH_PRODUCT);
            if (!StringUtils.isEmpty(json)) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray == null) {
                    return null;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject pobject = jsonArray.getJSONObject(i);
                    //注意  这里为了和数据库表同步  并且不影响 cid 统一把搜索Cid赋值为-1
                    Product pt = findProductById(pobject.optInt("ProductId"), pobject.optInt("SupplierId"));
                    if (pt == null) {
                        Product p = mGson.fromJson(pobject.toString(), Product.class);
                        p.CId = -1;
                        db.save(p);
                        lists.add(p);

                        //保存单个商品价格体系数据
                        if (pobject.has("PriceRule")) {
                            saveSimplePriceRules(pobject.optJSONArray("PriceRule"), pobject.optString("Unit"));
                        }

                    } else {
                        lists.add(pt);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * 以商品ID 更新数据
     *
     * @param productId
     * @param supplierId
     * @param productNum
     */
    public void updateProductNumById(int productId, int supplierId, int productNum) {
        try {
            Product product = findProductById(productId, supplierId);
            if (product != null) {
                //保存下单时间
                String[] columns = null;
                if (productNum == 1) {
                    product.AddCartTime = System.currentTimeMillis();
                    columns = new String[]{"ProductNum", "AddCartTime"};
                } else if (productNum == 0) {
                    product.AddCartTime = 0;
                    columns = new String[]{"ProductNum", "AddCartTime"};
                } else {
                    columns = new String[]{"ProductNum"};
                }
                product.ProductNum = productNum;
                db.update(product, columns);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return 获取单个商品  随着数量的增加而改变
     */
    public String getPrice(Product product) {
        List<PriceRules> rulesList = findPriceRulesById(product.SupplierId, product.ProductId);
        if (rulesList == null || rulesList.isEmpty() || product.ProductNum == 0) {
            return handlerPriceRules(product);
        }

        String simplePrice = "";

        for (PriceRules rules : rulesList) {
            if (product.ProductNum >= rules.MinQuantity && product.ProductNum <= rules.MaxQuantity) {
                simplePrice = getSimpleRulesData(product.IsDeduction, product.SaleMode, rules.Price, rules.Integral);
            } else if (product.ProductNum >= rules.MaxQuantity) {
                simplePrice = getSimpleRulesData(product.IsDeduction, product.SaleMode, rules.Price, rules.Integral);
            } else if (product.ProductNum < rules.MinQuantity) {
                simplePrice = handlerPriceRules(product);
            }
        }

        return simplePrice;
    }


    /**
     * @param isDeduction
     * @param price
     * @param integral
     * @return
     */
    public String getSimpleRulesData(String isDeduction, int saleMode, double price, double integral) {
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
     * 获取某商品下  所有货品的数量
     *
     * @param supplierId
     * @param productId
     */
    public int getGoodsNum(int supplierId, int productId) {
        int num = 0;
        try {
            List<Goods> goodsList = db.selector(Goods.class).where("SupplierId", "=", supplierId)
                    .and("ProductId", "=", productId)
                    .findAll();
            if (goodsList != null) {
                for (Goods goods : goodsList) {
                    num += goods.GoodsNumber;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * @param supplierId
     * @param productId
     * @return
     */
    public int getProductNum(int supplierId, int productId) {
        Product product = findProductById(productId, supplierId);
        if (product != null) {
            return product.ProductNum;
        }
        return 0;
    }

}
