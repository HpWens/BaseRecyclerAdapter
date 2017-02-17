package com.example.ecrbtb.mvp.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.detail.event.ImageEvent;
import com.example.ecrbtb.mvp.detail.presenter.ImagePresenter;
import com.example.ecrbtb.mvp.detail.view.IImageView;
import com.example.ecrbtb.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by boby on 2016/12/28.
 */

public class ImageFragment extends BasePageFragment implements IImageView {

    @InjectView(R.id.linear_image)
    LinearLayout mLinearImage;

    ImagePresenter mPresenter;

    public static ImageFragment newInstance() {

        Bundle args = new Bundle();

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new ImagePresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_detail_image;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull ImageEvent event) {
        String urls = event.urls;
        if (StringUtils.isEmpty(urls)) {
            TextView tv = new TextView(_mActivity);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
            tv.setGravity(Gravity.CENTER);
            tv.setText("无图文详解");
            mLinearImage.addView(tv);
        } else {
            mLinearImage.removeAllViews();
            String[] agrs = urls.split(",");
            for (String str : agrs) {
                ImageView imageView = new ImageView(_mActivity);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setTag(str);
                mLinearImage.addView(imageView);
                mPresenter.getImageUrlPixels(str);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        unregisterEventBus();
    }


    /**
     * @return
     */
    public int[] screenMetrics() {
        int[] pixels = new int[2];
        DisplayMetrics displayMetrics = _mActivity.getResources().getDisplayMetrics();
        pixels[0] = displayMetrics.widthPixels;
        pixels[1] = displayMetrics.heightPixels;
        return pixels;
    }

    @Override
    public void getBitmapSize(String url, int[] pixels) {
        int screenPixels[] = screenMetrics();
        float scale = (float) screenPixels[0] / pixels[0];
        int imageHeight = (int) (pixels[1] * scale);
        if (mLinearImage == null)
            return;
        ImageView iv = (ImageView) mLinearImage.findViewWithTag(url);
        if (iv != null && !StringUtils.isEmpty(url)) {
            iv.getLayoutParams().height = imageHeight;
            ImageLoader.getInstance().displayImage(url, iv);
        }
    }

    @Override
    public Context getImageContext() {
        return _mActivity;
    }
}
