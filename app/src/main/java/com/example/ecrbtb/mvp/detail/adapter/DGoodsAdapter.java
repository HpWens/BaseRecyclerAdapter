package com.example.ecrbtb.mvp.detail.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.example.ecrbtb.mvp.goods.adapter.IGoodsAdapter;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.utils.StringUtils;

import java.util.List;

/**
 * Created by boby on 2016/12/27.
 */

public class DGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private GoodsBiz mGoodsBiz;
    private CategoryBiz mCategoryBiz;
    private String mIsDeduction;

    private String mIsSingle;
    private IGoodsAdapter mListener;

    public DGoodsAdapter(Context context, int layoutResId, List<Goods> data, String isDeduction, String isSingle) {
        super(layoutResId, data);
        mGoodsBiz = GoodsBiz.getInstance(context);
        mCategoryBiz = CategoryBiz.getInstance(context);
        this.mIsDeduction = isDeduction;
        this.mIsSingle = isSingle == null ? "1" : isSingle;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Goods item) {
        helper.setText(R.id.tv_name, item.SpecValue);
        helper.setText(R.id.tv_stock, item.Stock + "");

//        if (mIsSingle.equals("1")) {
//            helper.setText(R.id.tv_num, mCategoryBiz.getProductNum(item.SupplierId, item.ProductId) + "");
//        } else {
        helper.setText(R.id.tv_num, item.GoodsNumber + "");
        //       }

        String[] rulesAndPrice = mGoodsBiz.getPriceRulesData(mIsDeduction, item.SaleMode, item);

        if (!StringUtils.isEmpty(rulesAndPrice[0])) {
            helper.setVisible(R.id.tv_rules, true);
            helper.setText(R.id.tv_rules, rulesAndPrice[0]);
        } else {
            helper.setVisible(R.id.tv_rules, false);
        }

        helper.setText(R.id.tv_price, item.GoodsNumber == 0 ? mGoodsBiz.getGoodsPriceData(mIsDeduction, item) : rulesAndPrice[1]);

        helper.setOnClickListener(R.id.iv_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.GoodsNumber += 1;
                notifyDataSetChanged();
            }
        });

        helper.setOnClickListener(R.id.iv_sub, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.GoodsNumber == 0) {
                    if (mListener != null) {
                        mListener.showNumLessZero();
                    }
                    return;
                }
                item.GoodsNumber -= 1;
                notifyDataSetChanged();
            }
        });
    }

    /**
     * @return
     */
    public int getGoodsNumber() {
        int num = 0;
        for (Goods goods : getData()) {
            num += goods.GoodsNumber;
        }
        return num;
    }

    public void setOnGoodsAdapterListener(IGoodsAdapter listener) {
        this.mListener = listener;
    }
}
