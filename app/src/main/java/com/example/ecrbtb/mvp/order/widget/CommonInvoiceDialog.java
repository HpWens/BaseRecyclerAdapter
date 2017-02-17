package com.example.ecrbtb.mvp.order.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.ecrbtb.R;
import com.example.ecrbtb.utils.StringUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

/**
 * Created by boby on 2017/1/5.
 */

public class CommonInvoiceDialog extends BottomBaseDialog<CommonInvoiceDialog> {

    Button mBtnCancel;

    Button mBtnConfirm;

    RadioGroup mRgType;

    RadioGroup mRgContent;

    EditText mEtRise;

    private int mType = 0;

    private String mContent = "明细";

    public CommonInvoiceDialog(Context context, View animateView) {
        super(context, animateView);
        init();
    }

    private void init() {
        widthScale(1.0f);
    }

    @Override
    public View onCreateView() {
        final View inflate = View.inflate(mContext, R.layout.dialog_common_invoice, null);
        mBtnCancel = (Button) inflate.findViewById(R.id.btn_cancel);
        mBtnConfirm = (Button) inflate.findViewById(R.id.btn_confirm);
        mEtRise = (EditText) inflate.findViewById(R.id.et_rise);
        mRgType = (RadioGroup) inflate.findViewById(R.id.rg_type);
        mRgContent = (RadioGroup) inflate.findViewById(R.id.rg_content);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_personal:
                        mType = 0;
                        break;
                    case R.id.rb_company:
                        mType = 1;
                        break;
                }
            }
        });
        mRgContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_detail:
                        mContent = "明细";
                        break;
                    case R.id.rb_work:
                        mContent = "办公用品";
                        break;
                    case R.id.rb_parts:
                        mContent = "配件";
                        break;
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rise = mEtRise.getText().toString();
                if (StringUtils.isEmpty(rise)) {
                    mEtRise.setError("抬头不能为空");
                } else {
                    if (listener != null) {
                        listener.onConfirmListener(mType, rise, mContent);
                        dismiss();
                    }
                }
            }
        });
    }

    private OnConfirmListener listener;

    public interface OnConfirmListener {
        void onConfirmListener(int type, String rise, String content);
    }

    public void setOnConfirmListener(OnConfirmListener confirmListener) {
        this.listener = confirmListener;
    }
}
