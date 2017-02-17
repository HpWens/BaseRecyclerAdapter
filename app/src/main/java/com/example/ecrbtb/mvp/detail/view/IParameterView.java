package com.example.ecrbtb.mvp.detail.view;

import android.content.Context;

import com.example.ecrbtb.mvp.detail.bean.ProductParams;

import java.util.List;

/**
 * Created by boby on 2016/12/28.
 */

public interface IParameterView {

    public Context getParameterContext();

    public void getParamsData(List<ProductParams> paramsList);
}
