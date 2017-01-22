package com.github.baserecycleradapter.entity;

import com.github.baserecycleradapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class SwipeCardBean {
    private int postition;
    private int resId;
    private String name;

    public SwipeCardBean(int postition, int resid, String name) {
        this.postition = postition;
        this.resId = resid;
        this.name = name;
    }

    public int getPostition() {
        return postition;
    }

    public SwipeCardBean setPostition(int postition) {
        this.postition = postition;
        return this;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public SwipeCardBean setName(String name) {
        this.name = name;
        return this;
    }

    public static List<SwipeCardBean> initDatas() {
        List<SwipeCardBean> datas = new ArrayList<>();
        int i = 1;
        datas.add(new SwipeCardBean(i++, R.mipmap.pic1, "张"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic2, "旭童"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic3, "多种type"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic4, "多种type"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic5, "多种type"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic6, "多种type"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic7, "多种type"));
        datas.add(new SwipeCardBean(i++, R.mipmap.pic8, "多种type"));
        return datas;
    }
}
