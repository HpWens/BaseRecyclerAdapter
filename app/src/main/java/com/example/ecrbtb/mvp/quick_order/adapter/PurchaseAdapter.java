package com.example.ecrbtb.mvp.quick_order.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.quick_order.bean.Order;
import com.example.ecrbtb.mvp.quick_order.bean.Purchase;
import com.example.ecrbtb.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/11.
 */

public class PurchaseAdapter extends BaseMultiItemQuickAdapter<Purchase, BaseViewHolder> {

    private Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PurchaseAdapter(Context context, List<Purchase> data) {
        super(data);
        this.mContext = context;

        addItemType(Order.HEADER, R.layout.item_purchase_header);
        addItemType(Order.CONTEXT, R.layout.item_purchase_content);
        addItemType(Order.FOOTER, R.layout.item_purchase_footer);

    }


    @Override
    protected void convert(BaseViewHolder helper, final Purchase item) {
        switch (helper.getItemViewType()) {
            case Order.HEADER:
                helper.setText(R.id.tv_number, "订单编号：" + item.OddNumber + '\n'
                        + "所属店铺：" + item.SupplierName);

                helper.setOnClickListener(R.id.card_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onStartDetail(Constants.PAY_BASE_URL + Constants.PURCHASE_DETAIL_URL + StringUtils.getBase64String(item.OddNumber));
                        }
                    }
                });
                break;
            case Order.CONTEXT:

                SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
                draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);

                helper.setText(R.id.tv_name, item.Name);
                helper.setText(R.id.tv_price, item.Price);
                helper.setText(R.id.tv_spec, item.SpecValue);
                helper.setText(R.id.tv_number, "X" + (int) item.Quantity);

                helper.setOnClickListener(R.id.card_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onStartDetail(Constants.PAY_BASE_URL + Constants.PURCHASE_DETAIL_URL + StringUtils.getBase64String(item.OddNumber));
                        }
                    }
                });
                break;
            case Order.FOOTER:

                helper.setText(R.id.tv_quantity, "共" + item.ItemCount + "件商品");
                helper.setText(R.id.tv_price, "合计：" + item.Price);

                helper.setOnClickListener(R.id.btn_pay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onAddShoppingCart(getPayData(item.Id));
                        }
                    }
                });

                break;
        }
    }


    /**
     * 获取再次购买数据
     *
     * @return
     */
    public List<Purchase> getPayData(int orderId) {
        List<Purchase> purchases = new ArrayList<>();
        for (Purchase purchase : getData()) {
            if (purchase.OrderId == orderId) {
                purchases.add(purchase);
            }
        }
        return purchases;
    }


    public OnPurchaseListener mListener;


    /**
     * 收藏订单接口
     */
    public interface OnPurchaseListener {

        public void onStartDetail(String url);

        public void onAddShoppingCart(List<Purchase> purchaseList);
    }

    /**
     * @param listener
     */
    public void setOnPurchaseListener(OnPurchaseListener listener) {
        this.mListener = listener;
    }
}
