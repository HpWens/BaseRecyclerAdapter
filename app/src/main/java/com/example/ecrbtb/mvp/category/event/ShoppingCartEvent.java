package com.example.ecrbtb.mvp.category.event;

/**
 * Created by boby on 2016/12/19.
 */

public class ShoppingCartEvent {

    public int num=0;

    public boolean isReset=false;

    public ShoppingCartEvent(int num) {
        this.num = num;
    }

    public ShoppingCartEvent(int num, boolean isReset) {
        this.num = num;
        this.isReset = isReset;
    }
}
