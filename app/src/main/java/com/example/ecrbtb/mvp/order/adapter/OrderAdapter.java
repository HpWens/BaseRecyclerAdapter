package com.example.ecrbtb.mvp.order.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by boby on 2017/1/5.
 */

public class OrderAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private boolean isShowInfo = true;

    public OrderAdapter(int layoutResId, List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Goods item) {

        if (helper.getAdapterPosition() < (getItemCount() - 1)) {
            //比较前一个和后一个 数据
            if (mData.get(helper.getAdapterPosition()).SupplierId == mData.get(helper.getAdapterPosition() + 1).SupplierId) {
                helper.setVisible(R.id.linear_discount, false);
                if (isShowInfo) {
                    helper.setVisible(R.id.tv_info, true);
                    isShowInfo = false;
                } else {
                    helper.setVisible(R.id.tv_info, false);
                }
            } else {
                helper.setVisible(R.id.linear_discount, true);
                if (isShowInfo) {
                    helper.setVisible(R.id.tv_info, true);
                } else {
                    helper.setVisible(R.id.tv_info, false);
                    isShowInfo = true;
                }
            }
        } else {
            if (isShowInfo) {
                helper.setVisible(R.id.tv_info, true);
            } else {
                helper.setVisible(R.id.tv_info, false);
            }
        }

        SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
        draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);

        helper.setText(R.id.tv_name, item.Name);
        helper.setText(R.id.tv_spec, item.SpecValue);
        helper.setText(R.id.tv_number, "X" + item.GoodsNumber);

        if (item.Price != 0 && item.SalesIntegral == 0) {
            helper.setText(R.id.tv_price, "¥" + item.Price);
        } else if (item.Price == 0 && item.SalesIntegral != 0) {
            helper.setText(R.id.tv_price, "积分" + item.SalesIntegral);
        } else {
            helper.setText(R.id.tv_price, "¥" + item.Price + "+积分" + item.SalesIntegral);
        }

        ((EditText) helper.getView(R.id.et_remarks)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mListener != null) {
                    mListener.onTextChangedListener(item.SupplierId, s.toString());
                }
            }
        });

        //优惠
        SpannableString spanDiscount = new SpannableString("优惠券：无可用");
        ForegroundColorSpan colorSpanDiscount = new ForegroundColorSpan(Color.parseColor("#2877b4"));
        spanDiscount.setSpan(colorSpanDiscount, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ((TextView) helper.getView(R.id.tv_discount)).setText(spanDiscount);

        //运费
        SpannableString spanFreight = new SpannableString("运费：¥" + item.Freight);
        ForegroundColorSpan colorSpanFreight = new ForegroundColorSpan(Color.parseColor("#ff4242"));
        spanFreight.setSpan(colorSpanFreight, 3, spanFreight.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ((TextView) helper.getView(R.id.tv_freight)).setText(spanFreight);

    }


    /**
     * @param supplierId
     * @return
     */
    public double getSupplierWeight(int supplierId) {
        double weight = 0;
        for (Goods goods : getData()) {
            if (goods.SupplierId == supplierId) {
                weight += goods.Weight;
            }
        }
        return weight;
    }


    private OnTextChangedListener mListener;

    public interface OnTextChangedListener {
        void onTextChangedListener(int supplierId, String remarks);
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        mListener = listener;
    }

}
