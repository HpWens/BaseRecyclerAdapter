package com.example.ecrbtb.mvp.shopping.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.mvp.shopping.bean.CartPrice;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by boby on 2016/12/22.
 */

public class ShoppingAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    private CategoryBiz mCategoryBiz;

    private IShopListener mListener;

    private GoodsBiz mGoodsBiz;

    //是否选中集合
    //private SparseBooleanArray mBooleanArray;

    public ShoppingAdapter(Context context, int layoutResId, List<Product> data) {
        super(layoutResId, data);
        mCategoryBiz = CategoryBiz.getInstance(context);
        mGoodsBiz = GoodsBiz.getInstance(context);

        //mBooleanArray = new SparseBooleanArray(data.size());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Product item) {

        helper.setText(R.id.tv_name, item.ProductName);
        SimpleDraweeView draweeView = helper.getView(R.id.simple_view);
        draweeView.setImageURI(Constants.BASE_URL + item.DefaultPic);
        helper.setText(R.id.tv_num, item.ProductNum + "");

        String[] rulesAndPrice = mGoodsBiz.getPriceRulesData(item.IsDeduction, item.SaleMode, item);

        if (IsSingle(item.IsSingle)) {
            helper.setText(R.id.tv_price, mCategoryBiz.getPrice(item));
        } else {
            helper.setText(R.id.tv_price, item.ProductNum == 0 ? mGoodsBiz.getGoodsPriceData(item.IsDeduction, item) : rulesAndPrice[1]);
        }

        helper.setOnClickListener(R.id.iv_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.ProductNum += 1;
                if (IsSingle(item.IsSingle)) {//单品处理
                    AddOrSubProduct(helper.getAdapterPosition(), item.ProductId, item.SupplierId, item.ProductNum);
                } else {  //多个货品处理
                    notifyDataSetChanged();
                }


                if (mListener != null) {
                    controlGoods(item.GoodsId, item.SupplierId, item.ProductNum);
                    mListener.addShoppingCart(item.ProductId);
                    //更新数量
                    mListener.cancelAllSelected(false);
                }
            }
        });

        helper.setOnClickListener(R.id.iv_sub, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item.ProductNum <= 1) {
                    if (mListener != null) {
                        mListener.showNumLessOne();
                    }
                    return;
                }

                item.ProductNum -= 1;

                if (IsSingle(item.IsSingle)) { //单品处理
                    AddOrSubProduct(helper.getAdapterPosition(), item.ProductId, item.SupplierId, item.ProductNum);

                } else { //处理多个货品的情况
                    controlGoods(item.GoodsId, item.SupplierId, item.ProductNum);
                    notifyDataSetChanged();
                }

                if (mListener != null) {
                    mListener.subShoppingCart(item.ProductId);
                    //更新数量
                    mListener.cancelAllSelected(false);
                }
            }
        });

        helper.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.showDeleteDialog(helper.getAdapterPosition(), item.SupplierId, item.ProductId, item.GoodsId, item.ProductNum, IsSingle(item.IsSingle));
                }
            }
        });

        helper.setOnClickListener(R.id.cb_check, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.IsChecked = !item.IsChecked;
                if (!item.IsChecked) {
                    if (mListener != null)
                        mListener.cancelAllSelected(true);
                } else {
                    if (mListener != null)
                        mListener.cancelAllSelected(false);
                }
            }
        });

        if (item.IsChecked) {
            ((CheckBox) helper.getView(R.id.cb_check)).setChecked(true);
        } else {
            ((CheckBox) helper.getView(R.id.cb_check)).setChecked(false);
        }

    }


    /**
     * 增加或者删除商品
     *
     * @param productId
     * @param productNum
     */
    private void AddOrSubProduct(int position, int productId, int supplierId, int productNum) {
        mCategoryBiz.updateProductNumById(productId, supplierId, productNum);
        //notifyItemChanged(position);
        //notifyItemChanged(position);
        notifyDataSetChanged();
    }

    /**
     * @param listener
     */
    public void setOnIShopListener(IShopListener listener) {
        this.mListener = listener;
    }


    /**
     * 判定是否是单品
     *
     * @param isSingle
     * @return
     */
    public boolean IsSingle(String isSingle) {
        return isSingle.equals("1") ? true : false;
    }


    /**
     * 处理购物车货品的增减删
     *
     * @param goodsId
     * @param supplierId
     * @param goodsNumber
     */
    public void controlGoods(int goodsId, int supplierId, int goodsNumber) {
        mGoodsBiz.updateGoodsNumById(goodsId, supplierId, goodsNumber);
    }

    /**
     * 显示选中checkbox
     *
     * @param position
     */
    public void setSelectItem(int position) {
        mData.get(position).IsChecked = true;
        notifyItemChanged(position);
    }

    public void setAllSelected(boolean isSelected) {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).IsChecked = isSelected;
        }
        notifyDataSetChanged();
    }

    //获取选中的数量
    public int getSelectedNum() {
        int num = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).IsChecked) {
                num += mData.get(i).ProductNum;
            }
        }
        return num;
    }

    /**
     * 获取选中的数据
     *
     * @return
     */
    public List<Product> getSelectedData() {
        List<Product> datas = new ArrayList<>();
        for (Product product : mData) {
            if (product.IsChecked) {
                datas.add(product);
            }
        }
        return datas;
    }

    /**
     * 获取所有数量
     *
     * @return
     */
    public int getAllNum() {
        int num = 0;
        for (int i = 0; i < mData.size(); i++) {
            num += mData.get(i).ProductNum;
        }
        return num;
    }

    //是否全部是选中状态
    public boolean isAllSelected() {
        for (Product product : mData) {
            if (!product.IsChecked) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取购物车单价
     */
    public void getCartUnitPrice() {
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                if (mData == null || mData.isEmpty()) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                        @Override
                        public void call() {
                            if (mListener != null) {
                                mListener.showEmptyData();
                            }
                        }
                    });
                } else {
                    List<CartPrice> priceList = new ArrayList<CartPrice>();
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).IsChecked) {
                            Product product = mData.get(i);
                            String[] rulesAndPrice = mGoodsBiz.getPriceRulesData(product.IsDeduction, product.SaleMode, product);
                            if (IsSingle(product.IsSingle)) {
                                CartPrice cartPrice = new CartPrice();
                                cartPrice.price = mCategoryBiz.getPrice(product);
                                cartPrice.num = product.ProductNum;
                                cartPrice.deductrate = product.Deductrate;
                                priceList.add(cartPrice);
                            } else {
                                CartPrice cartPrice = new CartPrice();
                                cartPrice.price = product.ProductNum == 0 ? mGoodsBiz.getGoodsPriceData(product.IsDeduction, product) : rulesAndPrice[1];
                                cartPrice.num = product.ProductNum;
                                cartPrice.deductrate = product.Deductrate;
                                priceList.add(cartPrice);
                            }

                        }
                    }

                    double price = 0;
                    double integral = 0;
                    //"¥"
                    for (CartPrice cartPrice : priceList) {
                        //¥+积分
                        String str = cartPrice.price;
                        if (str.startsWith("¥")) {
                            if (str.contains("+")) {
                                price += (double) Math.round(Double.parseDouble(str.substring(str.indexOf("¥") + 1, str.indexOf("+"))) * cartPrice.num * 100) / 100;
                                integral += (double) Math.round(Double.parseDouble(str.substring(str.indexOf("+") + 1, str.indexOf("积"))) * cartPrice.num * 100) / 100;
                            } else if (str.contains("可抵")) {
                                //可抵未做处理
                                double p = Double.parseDouble(str.substring(str.indexOf("¥") + 1, str.indexOf("可")));
                                double i = Double.parseDouble(str.substring(str.indexOf("抵") + 1, str.indexOf("积")));

                                price += (double) Math.round((p - i * cartPrice.deductrate) * cartPrice.num * 100) / 100;

                                integral += i * cartPrice.num;

                            } else {
                                price += (double) Math.round(Double.parseDouble(str.substring(str.indexOf("¥") + 1, str.length())) * cartPrice.num * 100) / 100;
                            }
                        } else {
                            integral += (double) Math.round(Double.parseDouble(str.substring(0, str.indexOf("积"))) * cartPrice.num * 100) / 100;
                        }
                    }

                    String resultPrice = "";

                    //处理多位小数位2位小数
                    String priceStr = price + "";
                    if (priceStr.contains(".")) {
                        if (priceStr.substring(priceStr.indexOf(".") + 1, priceStr.length()).length() > 2) {
                            priceStr = (float) Math.round(Float.parseFloat(priceStr) * 100) / 100 + "";
                        }
                    }

                    if (price == 0 && integral == 0) {
                        resultPrice = "¥ 0.00";
                    } else if (price == 0 && integral != 0) {
                        resultPrice = integral + "积分";
                    } else if (price != 0 && integral == 0) {
                        resultPrice = "¥" + priceStr;
                    } else {
                        resultPrice = "¥" + priceStr + " + " + integral + "积分";
                    }

                    final String cartPrice = resultPrice;
                    AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                        @Override
                        public void call() {
                            if (mListener != null) {
                                mListener.getCartPrice(cartPrice);
                            }
                        }
                    });
                }
            }
        });
    }

}
