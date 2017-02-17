package com.example.ecrbtb.mvp.detail.view;

import android.content.Context;

/**
 * Created by boby on 2017/1/9.
 */

public interface IImageView {

    public void getBitmapSize(String url, int pixels[]);

    public Context getImageContext();

}
