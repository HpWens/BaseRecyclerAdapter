package com.example.ecrbtb.mvp.order.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.order.bean.Address;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/5.
 */

public class ShoppingAddressDialog extends BottomBaseDialog<ShoppingAddressDialog> {

    private ArrayList<Address> mContents = new ArrayList<>();

    private OnOperItemClickL mOnOperItemClickL;
    private LayoutAnimationController mLac;

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    private int mLastCheckedPosition = -1;

    private OnSelectedListener mListener;

    public void setOnOperItemClickL(OnOperItemClickL onOperItemClickL) {
        mOnOperItemClickL = onOperItemClickL;
    }

    public ShoppingAddressDialog(Context context, List<Address> items, View animateView) {
        super(context, animateView);
        mContents.addAll(items);
        init();
    }

    private void init() {
        widthScale(1.0f);
    }


    @Override
    public View onCreateView() {
        final View inflate = View.inflate(mContext, R.layout.dialog_shopping_address, null);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflate.getContext()));
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<Address, BaseViewHolder>(R.layout.item_shopping_address, mContents) {

            @Override
            protected void convert(final BaseViewHolder helper, final Address item) {
                helper.setText(R.id.tv_name, item.Name);
                helper.setText(R.id.tv_phone, item.Mobile);
                helper.setText(R.id.tv_address, item.Province + item.City + item.Area + item.Address);

                helper.setOnClickListener(R.id.linear_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemChecked(helper.getAdapterPosition());
                        if (mListener != null) {
                            mListener.onSelectedListener(mLastCheckedPosition, item);
                        }
                    }
                });

                if (item.IsChecked) {
                    helper.setBackgroundRes(R.id.linear_item, R.drawable.ic_btn);
                } else {
                    helper.setBackgroundRes(R.id.linear_item, 0);
                }
            }
        });


        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }

    /**
     * @param position
     */
    public void setItemChecked(int position) {

        if (position == mLastCheckedPosition) {
            return;
        }

        List<Address> addressList = mAdapter.getData();
        addressList.get(position).IsChecked = true;

        if (mLastCheckedPosition > -1) {
            addressList.get(mLastCheckedPosition).IsChecked = false;
            mAdapter.notifyItemChanged(mLastCheckedPosition);
        }
        mAdapter.notifyDataSetChanged();

        mLastCheckedPosition = position;

    }

    /**
     * @return
     */
    public int getLastCheckedPosition() {
        return mLastCheckedPosition;
    }


    public interface OnSelectedListener {
        void onSelectedListener(int position, Address shoppingAddress);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mListener = listener;
    }

}
