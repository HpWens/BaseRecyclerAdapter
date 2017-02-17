package com.example.ecrbtb.mvp.quick_order.widget;

import android.content.Context;
import android.view.View;

import com.example.ecrbtb.R;
import com.flyco.dialog.widget.base.BottomBaseDialog;

/**
 * Created by boby on 2017/1/13.
 */

public class GoodsDialog extends BottomBaseDialog<GoodsDialog> {

    public GoodsDialog(Context context, View animateView) {
        super(context, animateView);
        widthScale(1.0f);
    }

    @Override
    public View onCreateView() {
        final View inflate = View.inflate(mContext, R.layout.dialog_add_address, null);
        return null;
    }

    @Override
    public void setUiBeforShow() {

    }


}
