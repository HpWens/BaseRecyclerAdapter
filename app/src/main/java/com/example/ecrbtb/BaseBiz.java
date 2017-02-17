package com.example.ecrbtb;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by boby on 2016/12/9.
 */

public class BaseBiz {

    protected Gson mGson;
    protected static Context mContext;
    protected SharedPreferences prefer;
    protected String TAG;

    private DbManager.DaoConfig daoConfig;

    protected DbManager db;

    public BaseBiz(Context context) {
        initDB();
        init(context);
    }

    private void init(Context context) {
        this.mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.mContext = context;
        this.prefer = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        this.TAG = this.getClass().getSimpleName();
    }

    private void initDB() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName(Constants.DB_NAME)
                // 不设置dbDir时, 默认存储在app的私有目录.
                //.setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(Constants.DB_VERSION)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        try {
                            db.dropDb();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
        db = x.getDb(daoConfig);
    }

    protected void showToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }


    //价格体系处理  基本所有的价格逻辑都涉及
    protected String handlerPriceRules(Product item) {
        String result = "";
        switch (item.PriceType) {
            default:
            case "-1":
                result = "¥" + item.Price;
                break;
            case "1":
                if (item.IsDeduction.equals("1")) {//可抵
                    result = getRulesByDeduction(item.SalesPrice, item.SalesIntegral);
                } else {  // == 0 不可抵积分
                    result = getRulesBySaleMode(item.SaleMode, item.SalesPrice, item.SalesIntegral);
                }
                break;
            case "0":
                if (item.IsDeduction.equals("1")) { //可抵金额
                    result = getRulesByDeduction(item.MinPrice, item.MinIntegral);
                } else {
                    result = getRulesBySaleMode(item.SaleMode, item.MinPrice, item.MinIntegral);
                }
                break;
        }
        return result;
    }

    /**
     * @param price
     * @param integral
     * @return
     */
    protected String getRulesByDeduction(double price, double integral) {
        return "¥" + price + (integral > 0 ? "可抵" + integral + "积分" : "");
    }

    /**
     * @param saleMode
     * @param price
     * @param integral
     * @return
     */
    protected String getRulesBySaleMode(int saleMode, double price, double integral) {
        String rules = "";
        switch (saleMode) {
            case 1:
                rules = integral + "积分";
                break;
            case 2:
                rules = "¥" + price;
                rules += integral > 0 ? "+" + integral + "积分" : "";
                break;
            case 0:
            default:
                rules = "¥" + price + "";
                break;
        }
        return rules;
    }


    //获取 token
    public String getToken() {
        return prefer.getString(Constants.TOKEN, "");
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return prefer.getInt(Constants.STORE_ID, 0) == 0 ? false : true;
    }

    /**
     * 获取门店ID
     *
     * @return
     */
    public int getStoreId() {
        return prefer.getInt(Constants.STORE_ID, 0);
    }


    /**
     * 获取管理员ID
     *
     * @return
     */
    public int getManagerId() {
        return prefer.getInt(Constants.MANAGER_ID, 0);
    }


    /**
     * //组装订单数据,详情界面 进货车 都需要用到订单数据
     *
     * @param goodsList
     * @param buyType   Cart  Product
     * @return
     */
    public String getCommitOrderData(List<Goods> goodsList, String buyType) {
        StringBuilder sb = new StringBuilder();
        if (goodsList == null || goodsList.isEmpty()) {
            return "";
        }
        for (Goods goods : goodsList) {
            if (goods.GoodsNumber == 0)
                continue;
            sb.append("<Goods>");
            sb.append("<SupplierId>" + goods.SupplierId + "</SupplierId>");
            sb.append("<BuyTypeId>0</BuyTypeId>");
            sb.append("<BuyType>" + buyType + "</BuyType>");
            sb.append("<productid>" + goods.ProductId + "</productid>");
            sb.append("<goodsid>" + goods.Id + "</goodsid>");
            sb.append("<specvalue></specvalue>");
            sb.append("<quantity>" + goods.GoodsNumber + "</quantity>");
            sb.append("<depotid>0</depotid>");
            sb.append("</Goods>");
        }
        return sb.toString();
    }
}
