package com.example.ecrbtb.mvp.detail.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.detail.bean.ProductParams;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
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
 * Created by boby on 2016/12/28.
 */

public class ParameterBiz extends BaseBiz {


    public ParameterBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static ParameterBiz INSTANCE = new ParameterBiz(mContext);
    }

    public static ParameterBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return ParameterBiz.SingletonHolder.INSTANCE;
    }

    /**
     * @param hm
     */
    public void requestParamData(HashMap<String, String> hm, final IComResponse<ProductParams> iComResponse) {
        RetrofitClient.getInstance(mContext)
                .createBaseApi()
                .postInIo(Constants.PARAMS_URL, hm, new Subscriber<ResponseBody>() {
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
                                if (jsonStr.equals("false")) {

                                } else {
                                    List<ProductParams> paramsList = new ArrayList<ProductParams>();
                                    try {
                                        JSONArray jsonArray = new JSONArray(jsonStr);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject paramsObject = jsonArray.optJSONObject(i);

                                            JSONArray paramsArray = paramsObject.optJSONArray("Extends");
                                            for (int j = 0; j < paramsArray.length(); j++) {
                                                JSONObject valueObject = paramsArray.optJSONObject(j);
                                                ProductParams params = new ProductParams();
                                                params.Name = valueObject.optString("Name") + "：";
                                                params.Value = valueObject.optString("Value");
                                                paramsList.add(params);
                                            }

                                            JSONArray details = paramsObject.optJSONArray("Params");
                                            for (int j = 0; j < details.length(); j++) {
                                                JSONObject detailObject = details.optJSONObject(j);
                                                JSONArray values = detailObject.optJSONArray("Values");
                                                for (int k = 0; k < values.length(); k++) {
                                                    JSONObject valueObject = values.optJSONObject(k);
                                                    ProductParams params = new ProductParams();
                                                    params.Name = valueObject.optString("Name") + "：";
                                                    params.Value = valueObject.optString("Value");
                                                    paramsList.add(params);
                                                }
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    iComResponse.getResponseData(paramsList);
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
     * @return
     */
    public List<String> getParameterData(int productId, int supplierId) {
        List<String> dataList = new ArrayList<>();
        try {
            Product product = db.selector(Product.class).where("ProductId", "=", productId)
                    .and("SupplierId", "=", supplierId).findFirst();
            if (product != null) {
                dataList.add(product.BrandName);
                dataList.add("");
                dataList.add(product.CategoryName);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return dataList;
    }

}
