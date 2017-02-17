package com.example.ecrbtb.mvp.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.detail.adapter.DGoodsAdapter;
import com.example.ecrbtb.mvp.detail.adapter.IStartDetailListener;
import com.example.ecrbtb.mvp.detail.adapter.LikeAdapter;
import com.example.ecrbtb.mvp.detail.event.ImageEvent;
import com.example.ecrbtb.mvp.detail.event.ProductParameterEvent;
import com.example.ecrbtb.mvp.detail.holder.NetworkImageHolderView;
import com.example.ecrbtb.mvp.detail.presenter.DetailPresenter;
import com.example.ecrbtb.mvp.detail.view.IDetailView;
import com.example.ecrbtb.mvp.goods.adapter.IGoodsAdapter;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.event.UpdateProductEvent;
import com.example.ecrbtb.mvp.login.LoginActivity;
import com.example.ecrbtb.mvp.merchant.MerchantWebActivity;
import com.example.ecrbtb.mvp.order.OrderActivity;
import com.example.ecrbtb.utils.StringUtils;
import com.example.ecrbtb.widget.PageStateLayout;
import com.grasp.tint.SystemBarTintManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by boby on 2016/12/25.
 */

public class DetailActivity extends BaseActivity implements IDetailView {

    public static final String VIEW_NAME_IMAGE = "image";
    public static final String VIEW_NAME_TEXT = "text";
    public static final String PRODUCT_DATA = "product_data";

    @InjectView(R.id.tv_stock)
    TextView mTvStock;
    @InjectView(R.id.tv_bar_code)
    TextView mTvBarCode;
    @InjectView(R.id.tv_price)
    TextView mTvPrice;
    @InjectView(R.id.rv_rules)
    RecyclerView mRvRules;
    @InjectView(R.id.banner)
    ConvenientBanner mBanner;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.textView)
    TextView mTextView;
    @InjectView(R.id.textView2)
    TextView mTextView2;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.net_scroll)
    NestedScrollView mNestedScrollView;
    @InjectView(R.id.tv_image)
    TextView mTvImage;
    @InjectView(R.id.tv_para)
    TextView mTvPara;
    @InjectView(R.id.rv_like)
    RecyclerView mRvLike;
    @InjectView(R.id.tv_collection)
    TextView mTvCollection;

    private DGoodsAdapter mDGoodsAdapter;
    private LikeAdapter mLikeAdapter;
    private Product mProduct;
    private DetailPresenter mPresenter;

    private SupportFragment[] mFragments = new SupportFragment[2];
    private int mSelectionPosition = 0;
    private static final int DETAIL = 0;
    private static final int PARAMETER = 1;

    private boolean mIsCollection = false;

    @Override
    protected void initData() {
        super.initData();
        if (mProduct == null)
            return;
        ViewCompat.setTransitionName(mBanner, VIEW_NAME_IMAGE);
        ViewCompat.setTransitionName(mTvName, VIEW_NAME_TEXT);

        if (StringUtils.isEmpty(mProduct.ProductName)) {
            mTvName.setText(mProduct.ProductName);
            mToolbar.setTitle(mProduct.ProductName);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        mRvRules.setLayoutManager(layoutManager);
        mRvRules.setHasFixedSize(true);
        mRvRules.setNestedScrollingEnabled(false);
        mRvRules.setAdapter(mDGoodsAdapter = new DGoodsAdapter(this, R.layout.item_detail_rules, new ArrayList<Goods>(), mProduct.IsDeduction, mProduct.IsSingle));

        mPresenter.requestDetailData(mProduct);

        mDGoodsAdapter.setOnGoodsAdapterListener(new IGoodsAdapter() {
            @Override
            public void showNumLessZero() {
                showToast("数量不能小于0");
            }
        });

        mTvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectionPosition == DETAIL) {
                    return;
                }

                mTvImage.setTextColor(Color.parseColor("#F44336"));
                mTvPara.setTextColor(Color.BLACK);

                showHideFragment(mFragments[DETAIL], mFragments[PARAMETER]);

                mSelectionPosition = DETAIL;
            }
        });

        mTvPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectionPosition == PARAMETER) {
                    return;
                }

                mTvPara.setTextColor(Color.parseColor("#F44336"));
                mTvImage.setTextColor(Color.BLACK);

                showHideFragment(mFragments[PARAMETER], mFragments[DETAIL]);

                mSelectionPosition = PARAMETER;
            }
        });

        mPresenter.requestLikeData(mProduct);
    }


    @Override
    protected void initView(ViewDataBinding bind) {
        initImageLoader();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);

        mRvLike.setLayoutManager(gridLayoutManager);
        mRvLike.setHasFixedSize(true);
        mRvLike.setNestedScrollingEnabled(false);
        mRvLike.setAdapter(mLikeAdapter = new LikeAdapter(this, R.layout.item_detail_like, new ArrayList<Product>()));

        mLikeAdapter.setOnStartDetailListener(new IStartDetailListener() {
            @Override
            public void startDetailActivity(Product product) {
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.PRODUCT_DATA, product);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        if (intent != null) {
            mProduct = intent.getParcelableExtra(PRODUCT_DATA);
        }
    }

    @Override
    protected void initBarTint() {
        super.initBarTint();
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            mFragments[DETAIL] = ImageFragment.newInstance();
            mFragments[PARAMETER] = ParameterFragment.newInstance(mProduct.ProductId, mProduct.SupplierId);
            loadMultipleRootFragment(R.id.fl_image_para, DETAIL, mFragments);
        } else {
            mFragments[DETAIL] = findFragment(ImageFragment.class);
            mFragments[PARAMETER] = findFragment(ParameterFragment.class);
        }

    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new DetailPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_detail;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    public Context getDetailContext() {
        return this;
    }

    @Override
    public void showServerError() {
        if (MyApplication.getInstance().isConnected()) {
            showToast(R.string.server_exception);
        } else {
            showToast("客官,请检查您的网络连接");
        }
    }

    @Override
    public void getProductData(Product product) {
        mTvName.setText(product.ProductName);
        mToolbar.setTitle(product.ProductName);
        mTvBarCode.setText("条形码：" + product.BarCode);
        mTvStock.setText("" + product.Stock + "\n" + "总库存");
        showNormalPage();
    }

    @Override
    public void getBannerData(List<String> bannerList) {
        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, bannerList).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageTransformer(new ZoomOutTranformer()).setCanLoop(true);
        if (bannerList.size() > 1) {
            mBanner.startTurning(5000);
        }
        EventBus.getDefault().post(new ProductParameterEvent());
    }


    @Override
    public void showLoadPage() {
        showPageState(PageStateLayout.LOADING_STATE);
    }

    @Override
    public void showNormalPage() {
        showPageState(PageStateLayout.NORMAL_STATE);
    }

    @Override
    public void showNetError() {
        showPageState(PageStateLayout.EMPTY_STATE);
    }

    @Override
    public void getGoodsData(List<Goods> list, boolean moreRequest) {
        if (list == null || list.isEmpty()) {
            if (moreRequest)
                mPresenter.requestGoodsData(mProduct);
            return;
        }
        mDGoodsAdapter.setNewData(list);
        mNestedScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void getLikeData(List<Product> productList) {
        mLikeAdapter.setNewData(productList);
    }

    @Override
    public void showEmptyData() {
        showToast(R.string.empty_data);
    }

    @Override
    public void updateShoppingCartNum(int num) {
        //更新货品数量
        EventBus.getDefault().post(new ShoppingCartEvent(num - mProduct.ProductNum));
        //更新商品数量
        EventBus.getDefault().post(new UpdateProductEvent(num, -1, mProduct.SupplierId, mProduct.ProductId));
        finish();
    }

    @Override
    public void getPhotoUrls(String urls) {
        EventBus.getDefault().post(new ImageEvent(urls));
    }

    @Override
    public void showNetErrorToast() {
        showToast("亲,你的网络不给力哟!");
    }

    @Override
    public void showCommitDataLoad() {
        showSweetAlertDialog("载入中...");
    }

    @Override
    public void dismissDataLoad() {
        dismissSweetAlertDialog();
    }

    @Override
    public void startOrderActivity(String json) {
        dismissDataLoad();
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.ORDER_DATA, json);
        startActivity(intent);
    }

    @Override
    public void showResponseError(String error) {
        dismissDataLoad();
        showToast(error);
    }

    @Override
    public void showCollectionResult(String result, boolean isCollection) {
        if (result.contains("-")) {
            if (result.equals("-11")) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                showToast("服务器响应失败");
            }
        } else {
            if (isCollection) {
                showToast("商品收藏成功!");
            } else {
                showToast("取消收藏成功!");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopTurning();
    }

    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_empty_page)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @OnClick({R.id.tv_cart, R.id.tv_pay, R.id.tv_custom, R.id.tv_collection, R.id.tv_shopping_cart})
    public void onClick(View view) {
        int num = mDGoodsAdapter.getGoodsNumber();
        switch (view.getId()) {
            case R.id.tv_cart:
                if (num == 0) {
                    showToast(R.string.empty_num);
                    return;
                }
                if (mProduct.IsSingle.equals("1")) {
                    mPresenter.updateProductNumById(mProduct.ProductId, mProduct.SupplierId, num);
                    EventBus.getDefault().post(new ShoppingCartEvent(num - mProduct.ProductNum));
                    //更新商品列表数据
                    EventBus.getDefault().post(new UpdateProductEvent(num, -1, mProduct.SupplierId, mProduct.ProductId));

                    showToast("加入进货车成功!");
                    finish();
                } else {
                    mPresenter.updateGoodsNumById(mProduct.ProductId, mProduct.SupplierId, mDGoodsAdapter.getData());
                }

                break;
            case R.id.tv_pay:
                if (num == 0) {
                    showToast(R.string.empty_order);
                    return;
                }
                mPresenter.commitOrderData(mDGoodsAdapter.getData(), Constants.BUY_TYPE_PRODUCT);
                break;
            case R.id.tv_custom:
                showToast("功能正在研发,客官请稍等");
                break;
            case R.id.tv_collection:
                mIsCollection = !mIsCollection;

                int resId = 0;

                if (mIsCollection) {
                    resId = R.mipmap.ic_collected;
                } else {
                    resId = R.mipmap.ic_collection;
                }

                Drawable drawableTop = this.getResources().getDrawable(resId);
                drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());

                if (mIsCollection) {
                    mTvCollection.setCompoundDrawables(null, drawableTop, null, null);
                    ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 1.5f, 1.0f);
                    animator.setDuration(500);
                    animator.start();
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            mTvCollection.setScaleX(value);
                            mTvCollection.setScaleY(value);
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            mTvCollection.setEnabled(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mTvCollection.setEnabled(true);
                        }
                    });
                } else {
                    mTvCollection.setCompoundDrawables(null, drawableTop, null, null);
                }
                mPresenter.requestCollection(mIsCollection, mProduct.SupplierId, mProduct.ProductId);
                break;
            case R.id.tv_shopping_cart:
                Intent intent = new Intent(this, MerchantWebActivity.class);
                intent.putExtra(Constants.MERCHANT_URL, Constants.PAY_BASE_URL + Constants.STORE_CART_URL);
                startActivity(intent);
                break;

        }
    }
}
