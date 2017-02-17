package com.example.ecrbtb.mvp.quick_order.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by boby on 2017/1/11.
 */

public class Purchase implements MultiItemEntity {

    public static final int HEADER = 1;
    public static final int CONTEXT = 2;
    public static final int FOOTER = 3;

    public int itemType;

    public String OddNumber;

    public String ItemCount;

    public double TotalAmount;

    public int TotalIntegral;

    public String DefaultPic;

    public String SpecValue;

    public double Quantity;

    public double ProductAmount;

    public String Price;

    public String Name;

    public String SupplierName;

    public boolean IsSingle;

    public int Id;

    public int OrderId;

    public int SupplierId;

    @Override
    public int getItemType() {
        return itemType;
    }


    public Purchase() {
        OddNumber = "";
        Id = 0;
        OrderId = 0;
        SupplierId = 0;
        IsSingle = false;
    }
}
