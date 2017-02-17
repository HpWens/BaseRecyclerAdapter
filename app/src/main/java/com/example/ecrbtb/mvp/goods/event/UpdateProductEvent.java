package com.example.ecrbtb.mvp.goods.event;

/**
 * Created by boby on 2016/12/20.
 */

public class UpdateProductEvent {

    public int num;

    public int position;

    public int productId;

    public int supplierId;

    public UpdateProductEvent(int num, int position) {
        this.num = num;
        this.position = position;
    }

    public UpdateProductEvent(int num, int position, int supplierId, int productId) {
        this.num = num;
        this.position = position;
        this.productId = productId;
        this.supplierId = supplierId;
    }
}
