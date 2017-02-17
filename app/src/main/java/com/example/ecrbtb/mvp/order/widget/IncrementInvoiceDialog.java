package com.example.ecrbtb.mvp.order.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecrbtb.R;
import com.example.ecrbtb.utils.RegularUtils;
import com.example.ecrbtb.utils.StringUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

/**
 * Created by boby on 2017/1/5.
 */

public class IncrementInvoiceDialog extends BottomBaseDialog<IncrementInvoiceDialog> {


    EditText mEtUnit;

    EditText mEtCode;

    EditText mEtAddress;

    EditText mEtPhone;

    EditText mEtBank;

    EditText mEtAccount;

    EditText mEtSend;

    Button mBtnCancel;

    Button mBtnConfirm;

    private Context mContext;

    public IncrementInvoiceDialog(Context context, View animateView) {
        super(context, animateView);
        this.mContext = context;
        init();
    }

    private void init() {
        widthScale(1.0f);
    }

    @Override
    public View onCreateView() {
        final View inflate = View.inflate(mContext, R.layout.dialog_increment_invoice, null);

        mEtUnit = (EditText) inflate.findViewById(R.id.et_unit);
        mEtCode = (EditText) inflate.findViewById(R.id.et_code);
        mEtAddress = (EditText) inflate.findViewById(R.id.et_address);
        mEtPhone = (EditText) inflate.findViewById(R.id.et_phone);
        mEtBank = (EditText) inflate.findViewById(R.id.et_bank);
        mEtAccount = (EditText) inflate.findViewById(R.id.et_account);
        mEtSend = (EditText) inflate.findViewById(R.id.et_send);
        mBtnCancel = (Button) inflate.findViewById(R.id.btn_cancel);
        mBtnConfirm = (Button) inflate.findViewById(R.id.btn_confirm);


        return inflate;
    }

    @Override
    public void setUiBeforShow() {

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String unit = mEtUnit.getText().toString();
                String code = mEtCode.getText().toString();
                String address = mEtAddress.getText().toString();
                String phone = mEtPhone.getText().toString();

                String bank = mEtBank.getText().toString();
                String account = mEtAccount.getText().toString();
                String send = mEtSend.getText().toString();


                RegularUtils regularUtils = RegularUtils.getRegular();

                if (StringUtils.isEmpty(unit)) {
                    mEtUnit.setError("单位名称不能为空");
                    Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(code)) {
                    mEtCode.setError("识别码不能为空");
                    Toast.makeText(mContext, "识别码不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(address)) {
                    mEtAddress.setError("注册地址不能为空");
                    Toast.makeText(mContext, "注册地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(phone)) {
                    mEtPhone.setError("注册电话不能为空");
                    Toast.makeText(mContext, "注册电话不能为空", Toast.LENGTH_SHORT).show();
                } else if (!regularUtils.isMatches(regularUtils.PatCellPhoneNum, phone)) {
                    mEtPhone.setError("手机号码格式不对");
                    Toast.makeText(mContext, "手机号码格式不对", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(bank)) {
                    mEtBank.setError("开户银行不能为空");
                    Toast.makeText(mContext, "开户银行不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(account)) {
                    mEtAccount.setError("银行账户不能为空");
                    Toast.makeText(mContext, "银行账户不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(send)) {
                    mEtSend.setError("寄送不能为空");
                    Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (listener != null) {
                        listener.onConfirmListener(new String[]{unit, code, address, phone, bank, account, send});
                        dismiss();
                    }
                }

            }
        });

    }

    private OnConfirmListener listener;

    public interface OnConfirmListener {
        void onConfirmListener(String[] invoices);
    }

    public void setOnConfirmListener(OnConfirmListener confirmListener) {
        this.listener = confirmListener;
    }
}
