package com.example.ecrbtb.mvp.goods.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.utils.StringUtils;

import java.util.List;

/**
 * Created by boby on 2016/12/20.
 */

public class GoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private Context mContext;
    private GoodsBiz mGoodsBiz;
    private IGoodsAdapter mListener;
    private String mIsDeduction;


    public GoodsAdapter(Context context, int layoutResId, List<Goods> data, String isDeduction) {
        super(layoutResId, data);
        this.mContext = context;
        this.mIsDeduction = isDeduction;
        this.mGoodsBiz = GoodsBiz.getInstance(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Goods item) {
        helper.setText(R.id.tv_spec, item.SpecValue);
        helper.setText(R.id.tv_num, item.GoodsNumber + "");

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
     * @param listener
     */
    public void setOnGoodsAdapterListener(IGoodsAdapter listener) {
        this.mListener = listener;
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

}
