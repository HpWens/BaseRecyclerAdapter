package com.chad.library.adapter.base.animation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;
import static com.chad.library.adapter.base.BaseQuickAdapter.CUSTOM;
import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;
import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_BOTTOM;
import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_BOTTOM_TOP;
import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT;
import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT_RIGHT;
import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_RIGHT;

/**
 * Created by boby on 2016/12/11.
 */
@IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT, SLIDEIN_LEFT_RIGHT, SLIDEIN_BOTTOM_TOP, CUSTOM})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnimationType {

}
