package com.example.ecrbtb.mvp.login.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.bean.PriceRules;
import com.example.ecrbtb.mvp.login.bean.LoginState;
import com.example.ecrbtb.mvp.login.bean.Manager;
import com.example.ecrbtb.mvp.login.bean.Store;
import com.example.ecrbtb.okhttp.RetrofitClient;
import com.example.ecrbtb.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by boby on 2016/12/13.
 */

public class UserBiz extends BaseBiz implements IUserBiz {

    private static Context mContext;

    public UserBiz(Context context) {
        super(context);
    }


    private static class SingletonHolder {
        private static UserBiz INSTANCE = new UserBiz(mContext);
    }

    public static UserBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return SingletonHolder.INSTANCE;
    }


    @Override
    public void login(String storeCode, String username, String password, final OnLoginListener loginListener) {

        HashMap<String, String> hm = new HashMap<>();

        hm.put("shopCode", storeCode);
        hm.put("userName", username);
        hm.put("password", password);

        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .post(Constants.LOGIN_URL, hm, new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loginListener.loginFailed();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        LoginState loginState = new LoginState();
                        try {
                            String resultJson = responseBody.string();
                            if (!StringUtils.isEmpty(resultJson)) {
                                JSONObject json = new JSONObject(resultJson);
                                if (!json.has("ErrMsg")) {
                                    Store store = mGson.fromJson(json.optString("store"), Store.class);
                                    if (store != null) {
                                        if (!findByStoreId(store.Id)) {
                                            db.save(store);
                                        }
                                    }

                                    Manager manager = mGson.fromJson(json.optString("manager"), Manager.class);
                                    if (manager != null) {
                                        if (!findByManagerId(manager.ManagerId)) {
                                            db.save(manager);
                                        }
                                    }

                                    loginState.state = 1;
                                    loginState.msg = "登录成功";

                                    //更新登录状态
                                    prefer.edit().putInt(Constants.STORE_ID, Integer.parseInt(store.Id)).commit();
                                    prefer.edit().putString(Constants.TOKEN, manager.Token).commit();

                                    db.dropTable(Product.class);
                                    db.dropTable(Goods.class);
                                    db.dropTable(PriceRules.class);

                                } else {
                                    loginState.state = 0;
                                    loginState.msg = json.optString("ErrMsg");
                                }
                            } else {
                                loginState.state = 0;
                                loginState.msg = "登录失败";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loginListener.loginSuccess(loginState);
                    }
                });

    }

    //id是否已经存在数据库中
    public boolean findByStoreId(String id) {
        try {
            Store store = db.selector(Store.class).where("StoreId", "=", id).findFirst();
            if (store == null) {
                return false;
            }
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean findByManagerId(int id) {
        try {
            Manager manager = db.selector(Manager.class).where("ManagerId", "=", id).findFirst();
            if (manager == null) {
                return false;
            }
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

}
