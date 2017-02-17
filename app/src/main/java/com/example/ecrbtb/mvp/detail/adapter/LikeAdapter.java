package com.example.ecrbtb.mvp.detail.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by boby on 2016/12/28.
 */

public class LikeAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    private IStartDetailListener mListener;

    public LikeAdapter(Context context, int layoutResId, List<Product> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final Product item) {

        helper.setText(R.id.tv_name, item.ProductName);
        helper.setText(R.id.tv_price, "¥" + item.SalesPrice + "");
        helper.setText(R.id.tv_sell, "已售：" + item.Sells + "");

        SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
        draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);

        helper.setOnClickListener(R.id.linear_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.startDetailActivity(item);
                }
            }
        });
    }

    /**
     * @param listener
     */
    public void setOnStartDetailListener(IStartDetailListener listener) {
        this.mListener = listener;
    }
}
