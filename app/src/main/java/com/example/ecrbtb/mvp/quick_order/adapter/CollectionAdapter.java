package com.example.ecrbtb.mvp.quick_order.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by boby on 2017/1/10.
 */

public class CollectionAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    private Context mContext;

    private CategoryBiz mCategoryBiz;

    public CollectionAdapter(Context context, int layoutResId, List<Product> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.mCategoryBiz = CategoryBiz.getInstance(context);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Product item) {

        helper.setText(R.id.tv_price, "¥" + item.Price);

        helper.setText(R.id.tv_name, item.ProductName);

        SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
        draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);

        if (item.ProductNum <= 0) {
            helper.setVisible(R.id.iv_sub, false);
            helper.setVisible(R.id.tv_num, false);
        } else {
            helper.setVisible(R.id.iv_sub, true);
            helper.setVisible(R.id.tv_num, true);
            helper.setText(R.id.tv_num, item.ProductNum + "");
        }

        helper.setOnClickListener(R.id.iv_sub, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    //未登录
                    if (mListener != null) {
                        mListener.onStartLogin();
                    }
                } else {
                    //登录状态  判断是否是单品
                    if (!isSingle(item.IsSingle)) {
                        //非单品
                        if (mListener != null) {
                            mListener.onStartGoods(helper.getAdapterPosition(), item);
                        }
                    } else {
                        //单品
                        item.ProductNum -= 1;

                        notifyItemData(item.SupplierId, item.ProductId, item.ProductNum);

                        if (mListener != null) {
                            mListener.onSubProduct();
                        }
                    }
                }
            }
        });


        helper.setOnClickListener(R.id.iv_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    //未登录
                    if (mListener != null) {
                        mListener.onStartLogin();
                    }
                } else {
                    //登录状态  判断是否是单品
                    if (!isSingle(item.IsSingle)) {
                        //非单品
                        if (mListener != null) {
                            mListener.onStartGoods(helper.getAdapterPosition(), item);
                        }
                    } else {
                        //单品
                        item.ProductNum += 1;

                        notifyItemData(item.SupplierId, item.ProductId, item.ProductNum);

                        if (mListener != null) {
                            mListener.onAddProduct();
                        }
                    }
                }
            }
        });


        helper.setOnClickListener(R.id.card_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onStartDetail(item);
                }
            }
        });

    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return mCategoryBiz.getStoreId() == 0 ? false : true;
    }

    /**
     * 判断是否是单品
     *
     * @param single
     * @return
     */
    public boolean isSingle(String single) {
        return single.equals("1") ? true : false;
    }


    /**
     * @param
     */
    public void notifyItemData(int supplierId, int productId, int productNum) {
        //更新本地数据库
        mCategoryBiz.updateProductNumById(productId, supplierId, productNum);

        closeLoadAnimation();
        notifyDataSetChanged();
        //openLoadAnimation();
    }


    public OnCollectionListener mListener;


    /**
     * 收藏订单接口
     */
    public interface OnCollectionListener {

        void onStartLogin();

        void onAddProduct();

        void onSubProduct();

        void onStartDetail(Product product);

        //pos  返回更新数据
        void onStartGoods(int position, Product product);
    }

    /**
     * @param listener
     */
    public void setOnCollectionListener(OnCollectionListener listener) {
        this.mListener = listener;
    }


}
