package com.example.ecrbtb.mvp.quick_order.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.Expose;

/**
 * Created by boby on 2017/1/11.
 */

public class Order implements MultiItemEntity {

    public static final int HEADER = 1;
    public static final int CONTEXT = 2;
    public static final int FOOTER = 3;

    public int itemType;

    /**
     * BuyRemark :
     * Id : 3410
     * FKFlag : 2
     * FKId : 66
     * OddNumber : 1612261949425387
     * Payables : 0.1
     * PayableIntegral : 0
     * PayTypeId : 2
     * PaymentId : 0
     * TotalAmount : 0.1
     * ProductAmount : 0.1
     * ProductDiscount : 0.0
     * OrderDiscount : 0.0
     * TotalIntegral : 0
     * PayStatus : 1
     * OrderStatus : 已完成
     * OrderTime : 2016/12/26 19:49:42
     * DispatchTime : 2016/12/26 20:14:03
     * InvType : 0
     * Status : 7
     * InvAddTime : 2016/12/26 19:49:42
     * ItemCount : 1
     * fkName : 福缘服装批发
     * IsRetreatAuthor : 0
     * RetreatCount : 1
     * IsComment : 1
     * OddNumbers : MTYxMjI2MTk0OTQyNTM4Nw==
     * EncryptOddNumber : MTYxMjI2MTk0OTQyNTM4Nw==
     */

    @Expose
    public String BuyRemark;

    @Expose
    public int Id;

    @Expose
    public int FKFlag;

    @Expose
    public int FKId;

    @Expose
    public String OddNumber;

    @Expose
    public double Payables;

    @Expose
    public int PayableIntegral;

    @Expose
    public int PayTypeId;

    @Expose
    public int PaymentId;

    @Expose
    public double TotalAmount;

    @Expose
    public double ProductAmount;

    @Expose
    public double ProductDiscount;

    @Expose
    public double OrderDiscount;

    @Expose
    public int TotalIntegral;

    @Expose
    public int PayStatus;

    @Expose
    public String OrderStatus;

    @Expose
    public String OrderTime;

    @Expose
    public String DispatchTime;

    @Expose
    public int InvType;

    @Expose
    public int Status;

    @Expose
    public String InvAddTime;

    @Expose
    public String ItemCount;

    @Expose
    public String fkName;

    @Expose
    public int IsRetreatAuthor;

    @Expose
    public String RetreatCount;

    @Expose
    public String IsComment;

    @Expose
    public String OddNumbers;

    @Expose
    public String EncryptOddNumber;

    @Override
    public int getItemType() {
        return itemType;
    }
}
