package com.example.ecrbtb.mvp.detail.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.example.ecrbtb.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    private SimpleDraweeView mDraweeView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        View view = View.inflate(context, R.layout.simple_drawee_view, null);
//        mDraweeView = (SimpleDraweeView) view.findViewById(R.id.simple_view);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, final String data) {
        imageView.setImageResource(R.drawable.ic_empty_page);
        ImageLoader.getInstance().displayImage(data, imageView);
        //mDraweeView.setImageURI(data);
    }
}
