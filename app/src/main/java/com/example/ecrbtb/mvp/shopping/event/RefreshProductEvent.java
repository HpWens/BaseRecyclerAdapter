package com.example.ecrbtb.mvp.shopping.event;

/**
 * Created by boby on 2016/12/23.
 */

public class RefreshProductEvent {

    public int productId;

    public int productNum;

    public RefreshProductEvent(int productId, int productNum) {
        this.productId = productId;
        this.productNum = productNum;
    }
}
