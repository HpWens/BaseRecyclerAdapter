package com.example.ecrbtb.mvp.merchant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecrbtb.BaseFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.event.TabSelectedEvent;
import com.example.ecrbtb.mvp.login.LoginActivity;
import com.example.ecrbtb.mvp.merchant.MerchantWebActivity;
import com.example.ecrbtb.mvp.merchant.bean.Merchant;
import com.example.ecrbtb.mvp.merchant.presenter.MerchantPresenter;
import com.example.ecrbtb.widget.RippleView;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by boby on 2016/12/29.
 */

public class MerchantContentFragment extends BaseFragment {

    private MerchantPresenter mPresenter;

    public static final int SALE_TYPE = 0;

    public static final int MANAGER_TYPE = 1;

    public static final String BASE_URL = "http://m.test.366ec.net/";

    int type = 0;

    @InjectView(R.id.gridview)
    GridView mGridView;

    List<Merchant> merchantList;

    private String[] mSaleNames = new String[]{"销售订单", "销售收入"
            , "零售会员", "店铺设置", "预览店铺", "申请提现", "核销", "线下收款", "退出登录"};

    //1 申请提现   0 退出登录
    private String[] mSaleUrl = new String[]{"Webstore/Order/List.aspx",
            "Webstore/Finance/InCome.aspx", "Webstore/Passport/List.aspx"
            , "Webstore/Shop/Setting.aspx", "Store/Default.aspx?sid="
            , "1", "Webstore/Order/Extract.aspx",
            "Webstore/Finance/ReceiveCash.aspx", "0"};

    private String[] mManagerNames = new String[]{"我要采购", "采购订单"
            , "采购授信", "采购积分", "采购清单", "退出登录"};

    //2 我要采购
    private String[] mManagerUrl = new String[]{"2", "Webstore/Supplier/OrderList.aspx"
            , "Webstore/Credit/MyCredit.aspx", "Webstore/Integral/MyIntegral.aspx"
            , "Webstore/Supplier/OftenBuyList.aspx", "0"};

    public static MerchantContentFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("Type", type);
        MerchantContentFragment fragment = new MerchantContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {

        return mPresenter = new MerchantPresenter(_mActivity);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        registerEventBus();

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("Type");
        }

        mGridView.setFocusable(false);
        mGridView.setSelection(0);

        if (type == 0) {
            merchantList = getSaleData();
        } else {
            merchantList = getManagerData();
        }

        mGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return merchantList.size();
            }

            @Override
            public Object getItem(int position) {
                return merchantList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                convertView = LayoutInflater.from(_mActivity).inflate(R.layout.item_merchat_gongge, null);
                if (holder != null) {
                    holder = (ViewHolder) convertView.getTag();
                } else {
                    holder = new ViewHolder();
                    holder.Name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.Icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holder.ItemView = (LinearLayout) convertView.findViewById(R.id.item_view);
                    holder.RippleLayout = (RippleView) convertView.findViewById(R.id.ripple_view);

                    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                    _mActivity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                    int mScreenWidth = localDisplayMetrics.widthPixels;

                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, mScreenWidth / 3);
                    convertView.setLayoutParams(params);

                    convertView.setTag(holder);
                }

                holder.Name.setText(merchantList.get(position).Name);
                holder.Icon.setImageResource(merchantList.get(position).Icon);
                holder.RippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        startMerchantWebActivity(position);
                    }
                });

                return convertView;
            }
        });
    }

    /**
     * @param position
     */
    public void startMerchantWebActivity(int position) {
        if (!MyApplication.getInstance().isLogin()) {
            startActivity(new Intent(_mActivity, LoginActivity.class));
            return;
        }
        Merchant merchant = merchantList.get(position);
        String url = merchant.Url;

        if (url.contains("Store/Default.aspx?sid=")) {
            url += mPresenter.getStoreId();
        }

        if (url.equals("0")) {
            showExitLoginDialog();
        } else if (url.equals("1")) {
            Snackbar.make(mGridView, "该功能正在开发当中...", Snackbar.LENGTH_SHORT).show();
        } else if (url.equals("2")) {
            EventBus.getDefault().post(new TabSelectedEvent(1));
        } else {
            Intent intent = new Intent(_mActivity, MerchantWebActivity.class);
            intent.putExtra(Constants.MERCHANT_URL, BASE_URL + url);
            startActivity(intent);
        }
    }

    /**
     * 退出登录
     */
    public void showExitLoginDialog() {
        final MaterialDialog dialog = new MaterialDialog(_mActivity);
        dialog//
                .btnNum(2)
                .content("你确定要注销登录吗?")//
                .btnText("取消", "确定")//
                .showAnim(new ZoomInEnter())//
                .dismissAnim(new ZoomInExit())//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                startActivity(new Intent(_mActivity, LoginActivity.class));
                dialog.dismiss();
            }
        });
    }


    private List<Merchant> getSaleData() {
        List<Merchant> merchants = new ArrayList<>();

        for (int i = 0; i < mSaleNames.length; i++) {
            Merchant merchant = new Merchant();
            merchant.Name = mSaleNames[i];
            merchant.Icon = R.mipmap.ic_merchant;
            merchant.Url = mSaleUrl[i];
            merchants.add(merchant);
        }
        return merchants;
    }


    private List<Merchant> getManagerData() {
        List<Merchant> merchants = new ArrayList<>();

        for (int i = 0; i < mManagerNames.length; i++) {
            Merchant merchant = new Merchant();
            merchant.Name = mManagerNames[i];
            merchant.Icon = R.mipmap.ic_merchant;
            merchant.Url = mManagerUrl[i];
            merchants.add(merchant);
        }
        return merchants;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_merchat_gongge;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus();
        ButterKnife.reset(this);
    }

    class ViewHolder {
        LinearLayout ItemView;
        TextView Name;
        ImageView Icon;
        RippleView RippleLayout;
    }

}
