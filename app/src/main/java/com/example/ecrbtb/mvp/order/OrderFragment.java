package com.example.ecrbtb.mvp.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecrbtb.BaseFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.merchant.MerchantWebActivity;
import com.example.ecrbtb.mvp.order.adapter.OrderAdapter;
import com.example.ecrbtb.mvp.order.bean.Address;
import com.example.ecrbtb.mvp.order.bean.Coupon;
import com.example.ecrbtb.mvp.order.bean.OrderData;
import com.example.ecrbtb.mvp.order.presenter.OrderPresenter;
import com.example.ecrbtb.mvp.order.view.IOrderView;
import com.example.ecrbtb.mvp.order.widget.AddAddressDialog;
import com.example.ecrbtb.mvp.order.widget.CommonInvoiceDialog;
import com.example.ecrbtb.mvp.order.widget.IncrementInvoiceDialog;
import com.example.ecrbtb.mvp.order.widget.ShoppingAddressDialog;
import com.example.ecrbtb.utils.KeyBoardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2017/1/4.
 */

public class OrderFragment extends BaseFragment implements IOrderView {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.btn_other_address)
    Button mBtnOtherAddress;
    @InjectView(R.id.tv)
    TextView mTv;
    @InjectView(R.id.fl_root)
    FrameLayout mFlRoot;
    @InjectView(R.id.btn_online)
    Button mBtnOnline;
    @InjectView(R.id.btn_line)
    Button mBtnLine;
    @InjectView(R.id.btn_account)
    Button mBtnAccount;
    @InjectView(R.id.linear_pay_mode)
    LinearLayout mLinearPayMode;
    @InjectView(R.id.btn_no_need)
    Button mBtnNoNeed;
    @InjectView(R.id.btn_common)
    Button mBtnCommon;
    @InjectView(R.id.btn_increment)
    Button mBtnIncrement;
    @InjectView(R.id.linear_invoice)
    LinearLayout mLinearInvoice;
    @InjectView(R.id.recycler_order)
    RecyclerView mRecyclerOrder;
    @InjectView(R.id.tv_address)
    TextView mTvAddress;
    @InjectView(R.id.tv_invoice)
    TextView mTvInvoice;
    @InjectView(R.id.tv_discount_price)
    TextView mTvDiscountPrice;
    @InjectView(R.id.tv_integral)
    TextView mTvIntegral;
    @InjectView(R.id.tv_payment)
    TextView mTvPayment;


    private OrderAdapter mAdapter;

    private OrderPresenter mPresenter;

    private List<Address> mAddressList = new ArrayList<>();

    private List<Integer> mSupplierIdsList = new ArrayList<>();

    //最原始数据
    private List<Double> mOriginalGoodsList = new ArrayList<>();

    private int mAddressId;

    //总金额
    private double mTotalPrice;

    //总积分
    private int mTotalIntegral;

    //总运费
    private double mTotalFreight;

    //总税率
    private double mTotalInvoice;

    //普通发票的税率
    private float mCommInvoice = 0.2f;

    //增值税的税率
    private float mIncreInvoice = 0.25f;

    //提交订单数据
    private String mProductIds = "";
    private String mProductInfo = "";
    private Address mAddress = new Address();
    private int mInvoiceType = 0;
    private int mPayType = 2;
    private List<Coupon> mCouponList = new ArrayList<>();

    private int mType = 0;
    private String mRise = "";
    private String mContent = "明细";

    private String[] mIncrementInvoices = new String[7];

    private static final String ORDER_DATA = "order_data";

    public static OrderFragment newInstance(String orderData) {
        Bundle args = new Bundle();
        args.putString(ORDER_DATA, orderData);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new OrderPresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_order_confirm;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mToolbar.setTitle("订单确定");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerOrder.setLayoutManager(layoutManager);
        mRecyclerOrder.setHasFixedSize(true);
        mRecyclerOrder.setNestedScrollingEnabled(false);
        mRecyclerOrder.setAdapter(mAdapter = new OrderAdapter(R.layout.item_order_product, new ArrayList<Goods>()));
        mAdapter.setOnTextChangedListener(new OrderAdapter.OnTextChangedListener() {
            @Override
            public void onTextChangedListener(int supplierId, String remarks) {
                for (Coupon coupon : mCouponList) {
                    if (coupon.SupplierId == supplierId) {
                        coupon.BuyRemark = remarks;
                    }
                }
            }
        });

        //获取数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            String json = bundle.getString(ORDER_DATA, "");
            mPresenter.handlerOrderData(json);
        }

        //获取地址地址列表 默认不打开收货地址提示框
        mPresenter.getAddressListData(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private int mCurrentPosition = -1;

    @OnClick({R.id.btn_other_address, R.id.tv_add_address,
            R.id.btn_online, R.id.btn_line, R.id.btn_account,
            R.id.btn_no_need, R.id.btn_common, R.id.btn_increment, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_other_address:
                if (mAddressList.isEmpty()) {
                    showToast("无收货地址");
                    return;
                }
                final ShoppingAddressDialog shoppingAddressDialog = new ShoppingAddressDialog(_mActivity, mAddressList, null);
                shoppingAddressDialog.show();
                shoppingAddressDialog.setItemChecked(mCurrentPosition);
                shoppingAddressDialog.setOnSelectedListener(new ShoppingAddressDialog.OnSelectedListener() {
                    @Override
                    public void onSelectedListener(int position, Address address) {
                        mAddress = address;
                        mCurrentPosition = position;
                        mAddressId = address.Id;
                        mTvAddress.setText(address.Name + "（" + address.Mobile + "）" + '\n' + address.Province + " " + address.City + " " + address.Area + " " + address.Address);
                        shoppingAddressDialog.dismiss();

                        //获取运费
                        mTotalFreight = 0;
                        for (Integer supplierId : mSupplierIdsList) {
                            mPresenter.getFreightData(supplierId, mAddressId, mAdapter.getSupplierWeight(supplierId));
                        }
                    }
                });
                break;

            case R.id.tv_add_address:

                final AddAddressDialog addAddressDialog = new AddAddressDialog(_mActivity, null);
                addAddressDialog.setCanceledOnTouchOutside(false);
                addAddressDialog.show();

                addAddressDialog.setOnAddAddress(new AddAddressDialog.IAddAddress() {
                    @Override
                    public void onIAddAddress(Address address) {
                        //请求接口新增地址
                        mPresenter.commitAddAddress(address);
                        KeyBoardUtil.closeKeyboard(addAddressDialog);
                    }
                });
                break;
            case R.id.btn_online:
                switchPayMode(mLinearPayMode, R.id.btn_online);
                mBtnNoNeed.performClick();

                mPayType = 2;
                break;
            case R.id.btn_line:
                switchPayMode(mLinearPayMode, R.id.btn_line);
                mBtnNoNeed.performClick();

                mPayType = 1;
                break;
            case R.id.btn_account:
                switchPayMode(mLinearPayMode, R.id.btn_account);
                mBtnNoNeed.performClick();

                mPayType = 3;
                break;
            case R.id.btn_no_need:
                switchPayMode(mLinearInvoice, R.id.btn_no_need);

                mInvoiceType = 0;

                //增加税率  税率为0
                updateInvoice(0);
                //计算实付款
                setPaymentText();

                break;
            case R.id.btn_common:
                final CommonInvoiceDialog commonInvoiceDialog = new CommonInvoiceDialog(_mActivity, null);
                commonInvoiceDialog.show();
                commonInvoiceDialog.setOnConfirmListener(new CommonInvoiceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmListener(int type, String rise, String content) {
                        switchPayMode(mLinearInvoice, R.id.btn_common);
                        KeyBoardUtil.closeKeyboard(commonInvoiceDialog);

                        mInvoiceType = 1;

                        mType = type;

                        mRise = rise;

                        mContent = content;

                        //增加税率
                        updateInvoice(mCommInvoice);
                        //计算实付款
                        setPaymentText();
                    }
                });
                break;
            case R.id.btn_increment:
                final IncrementInvoiceDialog incrementInvoiceDialog = new IncrementInvoiceDialog(_mActivity, null);
                incrementInvoiceDialog.show();
                incrementInvoiceDialog.setOnConfirmListener(new IncrementInvoiceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmListener(String[] invoices) {
                        mIncrementInvoices = invoices;
                        switchPayMode(mLinearInvoice, R.id.btn_increment);
                        KeyBoardUtil.closeKeyboard(incrementInvoiceDialog);

                        mInvoiceType = 2;

                        //增加税率
                        updateInvoice(mIncreInvoice);
                        //计算实付款
                        setPaymentText();
                    }
                });
                break;
            case R.id.tv_commit:
                if (mAddress.Id == 0) {
                    showToast("客官,请选择收货地址");
                    return;
                }

                OrderData.Builder builder = new OrderData.Builder();
                OrderData orderData = builder.ProductId(mProductIds)
                        .ProductInfo(mProductInfo)
                        .AddressData(mAddress)
                        .PayType(mPayType)
                        .PayPrice(mTotalPrice + mTotalFreight + mTotalInvoice)
                        .InvoiceType(mInvoiceType)
                        .Coupons(mCouponList)
                        .CommInvoiceType(mType)
                        .CommInvoiceRise(mRise)
                        .CommInvoiceContent(mContent)
                        .IncrementInvoices(mIncrementInvoices)
                        .build();

                mPresenter.commitOrderData(orderData);

                break;
        }
    }

    /**
     * 更新税率
     */
    public void updateInvoice(float invoice) {
        mTotalInvoice = 0;
        List<Goods> goodsList = mAdapter.getData();
        if (mOriginalGoodsList == null || mOriginalGoodsList.isEmpty()) {
            return;
        }
        for (int i = 0; i < goodsList.size(); i++) {
            Goods goods = goodsList.get(i);
            Double originalPrice = mOriginalGoodsList.get(i);
            if (originalPrice != 0) {
                goods.Price = originalPrice + Math.floor(originalPrice * invoice * 100) / 100;
                mTotalInvoice += Math.floor(originalPrice * invoice * 100) / 100;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 转换支付方式
     *
     * @param viewId
     */
    public void switchPayMode(ViewGroup viewGroup, int viewId) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewId == viewGroup.getChildAt(i).getId()) {
                viewGroup.getChildAt(i).setBackgroundResource(R.mipmap.ic_button_click_bg);
                ((Button) viewGroup.getChildAt(i)).setTextColor(Color.parseColor("#ff4242"));
            } else {
                viewGroup.getChildAt(i).setBackgroundResource(R.mipmap.ic_button_bg);
                ((Button) viewGroup.getChildAt(i)).setTextColor(Color.parseColor("#393939"));
            }
        }
        if (viewId == R.id.btn_no_need) {
            mTvInvoice.setText("你已经选择不开具发票！");
        } else if (viewId == R.id.btn_common) {
            mTvInvoice.setText("你已经选择开普通发票！");
        } else if (viewId == R.id.btn_increment) {
            mTvInvoice.setText("你已经选择开增值税发票！");
        }
    }

    @Override
    public Context getOrderContext() {
        return _mActivity;
    }

    @Override
    public void getAddressListData(List<Address> datas, boolean isShow) {
        mAddressList = datas;
        if (isShow) {
            mBtnOtherAddress.performClick();
        }
    }

    @Override
    public void showNetError() {
        showToast("客官,你的网络不给力!");
    }

    @Override
    public void showSweetDialog() {
        showSweetAlertDialog("正在提交...");
    }

    @Override
    public void showResponseError(String error) {
        showToast(error);
        dismissSweetAlertDialog();
    }

    @Override
    public void onSuccessAddAddress(String success) {
        showToast(success);
        //获取地址地址列表
        mPresenter.getAddressListData(true);
        dismissSweetAlertDialog();
    }

    @Override
    public void showOrderListData(List<Goods> goodsList) {
        mAdapter.setNewData(goodsList);
        //赋值原始数据
        for (Goods goods : goodsList) {
            mOriginalGoodsList.add(goods.Price);
        }
    }

    @Override
    public void getSuppliers(List<Integer> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return;
        }
        mSupplierIdsList = suppliers;
        //赋值给提交订单数据
        for (Integer supplierId : mSupplierIdsList) {
            Coupon coupon = new Coupon();
            coupon.SupplierId = supplierId;
            mCouponList.add(coupon);
        }
    }

    @Override
    public void getFreightData(int supplierId, String freight) {
        //更新运费
        List<Goods> goodsList = mAdapter.getData();
        for (Goods goods : goodsList) {
            if (goods.SupplierId == supplierId) {
                goods.Freight = freight;
            }
        }
        mAdapter.notifyDataSetChanged();
        //总运费
        mTotalFreight += Double.parseDouble(freight);
        //总金额
        setPaymentText();
    }

    @Override
    public void getPriceAndIntegral(double price, int integral) {
        mTotalPrice = price;
        mTotalIntegral = integral;
        setPaymentText();
    }

    @Override
    public void getProductIdsAndInfo(String productIds, String productInfo) {
        mProductIds = productIds;
        mProductInfo = productInfo;
    }

    @Override
    public void showCommitError(String error) {
        dismissSweetAlertDialog();
        showToast(error);
    }

    @Override
    public void showCommitSuccess(String success) {
        dismissSweetAlertDialog();

        Intent intent = new Intent(_mActivity, MerchantWebActivity.class);
        intent.putExtra(Constants.MERCHANT_URL, success);
        startActivity(intent);

        _mActivity.finish();
    }

    /**
     * 设置实付款
     */
    public void setPaymentText() {
        double price = mTotalPrice + mTotalFreight + mTotalInvoice;
        int integral = mTotalIntegral;

        String text = "";
        if (price != 0 && integral == 0) {
            text = "¥" + price;
        } else if (price == 0 && integral != 0) {
            text = "积分" + integral;
        } else {
            text = "¥" + price + "+积分" + integral;
        }

        //运费
        SpannableString span = new SpannableString("实付款：" + text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ff4242"));
        span.setSpan(colorSpan, 4, span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTvPayment.setText(span);
    }

}
