package com.example.ecrbtb.mvp.detail.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.mvp.detail.biz.DetailBiz;
import com.example.ecrbtb.mvp.detail.view.IImageView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by boby on 2017/1/9.
 */

public class ImagePresenter implements BasePresenter {

    private IImageView mIImageView;

    private DetailBiz mDetailBiz;

    public ImagePresenter(IImageView IImageView) {
        mIImageView = IImageView;
        mDetailBiz = DetailBiz.getInstance(mIImageView.getImageContext());
    }


    /**
     * @param url
     */
    public void getImageUrlPixels(final String url) {
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                final int[] pixels = mDetailBiz.getImageUrlPixels(url);
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIImageView.getBitmapSize(url, pixels);
                    }
                });
            }
        });
    }

}
