package com.example.ecrbtb.mvp.shopping.adapter;

/**
 * Created by boby on 2016/12/23.
 */

public interface IShopListener {

    void addShoppingCart(int productId);

    void subShoppingCart(int productId);

    void showNumLessOne();

    void showDeleteDialog(int position, int supplierId, int productId, int goodsId, int productNum, boolean isSingle);

    //取消全选状态
    void cancelAllSelected(boolean  isCancel);

    void showEmptyData();

    void getCartPrice(String cartPrice);
}
