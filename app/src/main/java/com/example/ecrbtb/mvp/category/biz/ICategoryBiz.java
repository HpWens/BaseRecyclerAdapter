package com.example.ecrbtb.mvp.category.biz;

import java.util.HashMap;

/**
 * Created by boby on 2016/12/16.
 */

public interface ICategoryBiz {

    void requestProductData(HashMap<String, String> hm, boolean isSearch, IResponseListener listener);

    void requestCategoryDate(HashMap<String, String> hm, IResponseListener listener);


}
