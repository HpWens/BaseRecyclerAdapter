package com.example.ecrbtb.mvp.category.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by boby on 2016/12/16.
 */

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    private IAddProductListener mListener;

    private static final String HAS_SPEC = "1";
    private static final String NO_SPEC = "0";

    private ViewGroup mViewGroup;

    private CategoryBiz mCategoryBiz;

    private Context mCtx;

    public ProductAdapter(Context context, int layoutResId, int transLayoutResId, List<Product> lists) {
        super(layoutResId, transLayoutResId, lists);
        this.mCtx = context;
        mCategoryBiz = CategoryBiz.getInstance(mCtx);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Product item) {
        helper.setText(R.id.tv_name, item.ProductName);

        if (!mCategoryBiz.isLogin()) {
            helper.setText(R.id.tv_price, "¥" + item.Price);
        } else {
            helper.setText(R.id.tv_price, mCategoryBiz.getPrice(item));
        }

        if (item.ProductNum <= 0) {
            helper.setVisible(R.id.iv_sub, false);
            helper.setVisible(R.id.tv_num, false);
        } else {
            helper.setVisible(R.id.iv_sub, true);
            helper.setVisible(R.id.tv_num, true);
//            if (item.IsSingle.equals("1")) {
            helper.setText(R.id.tv_num, item.ProductNum + "");
//            } else {
//                helper.setText(R.id.tv_num, mCategoryBiz.getGoodsNum(item.SupplierId, item.ProductId) + "");
//            }
        }
        SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
        draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);


        helper.setOnClickListener(R.id.iv_sub, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getStoreId() == 0) {
                    if (mListener != null) {
                        mListener.startLogin();
                    }
                } else {
                    if (item.IsSingle.equals(NO_SPEC)) {
                        if (mListener != null) {
                            mListener.startGoodsPage(helper.getAdapterPosition(), item);
                        }
                    } else if (item.IsSingle.equals(HAS_SPEC)) {
                        item.ProductNum -= 1;
                        AddOrSubProduct(helper.getAdapterPosition(), item.ProductId, item.SupplierId, item.ProductNum);
                        if (mListener != null) {
                            mListener.subShoppingCart();
                        }
                    }
                }
            }
        });

        helper.setOnClickListener(R.id.iv_add, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (MyApplication.getInstance().getStoreId() == 0) {
                    if (mListener != null) {
                        mListener.startLogin();
                    }
                } else {
                    if (item.IsSingle.equals(NO_SPEC)) {

                        if (mListener != null) {
                            mListener.startGoodsPage(helper.getAdapterPosition(), item);
                        }

                    } else if (item.IsSingle.equals(HAS_SPEC)) {
                        item.ProductNum += 1;
                        AddOrSubProduct(helper.getAdapterPosition(), item.ProductId, item.SupplierId, item.ProductNum);

                        if (mListener != null) {
                            mListener.addShoppingCart();
                        }

                        int[] location = new int[2];
                        v.getLocationOnScreen(location);
                        int[] offset = getOffset(location);

                        TextView textView = new TextView(mContext);
                        textView.setBackgroundResource(R.drawable.bg_red_point);
                        textView.setText("1");
                        textView.setTextSize(12);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.WHITE);

                        setAnim(textView, location, offset);
                    }
                }
            }
        });

        helper.setOnClickListener(R.id.fl_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.startDetailActivity(helper.getView(R.id.simple_view), helper.getView(R.id.tv_name), item);
                }
            }
        });

    }


    /**
     * 增加或者删除商品
     *
     * @param productId
     * @param productNum
     */
    private void AddOrSubProduct(int position, int productId, int supplierId, int productNum) {
        mCategoryBiz.updateProductNumById(productId, supplierId, productNum);
        closeLoadAnimation();
        //notifyItemChanged(position);
        notifyDataSetChanged();
    }


    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    public int[] getPixels() {
        int[] pixels = new int[2];
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        pixels[0] = displayMetrics.widthPixels;
        pixels[1] = displayMetrics.heightPixels;
        return pixels;
    }

    public int[] getOffset(int[] location) {
        int[] offsets = new int[2];

        int[] pixels = getPixels();

        int width = pixels[0] * 7 / 10;
        int height = pixels[1] - dip2px(48);

        offsets[0] = width - location[0];
        offsets[1] = height - location[1];
        return offsets;
    }

    public void setAnim(final View v, int[] startLocation, int[] offset) {
        mViewGroup = null;
        mViewGroup = createAnimLayout();
        mViewGroup.removeAllViews();
        mViewGroup.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(v,
                startLocation);

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                offset[0], 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, offset[1]);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setIAddProductListener(IAddProductListener listener) {
        this.mListener = listener;
    }


}
