package com.example.ecrbtb.mvp.quick_order.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2017/1/11.
 */
@Table(name = "OrderItem")
public class OrderItem implements Parcelable {


    /**
     * OrderId : 3133
     * ParentId : 0
     * GoodsId : 1536
     * ProductId : 749
     * Name : 测试商品123[规格:箱 ]
     * GoodsCode : WIVN1612011645276867097_00
     * SpecValue : 规格:箱
     * Quantity : 1.0
     * Weight : 0.1
     * DefaultPic : /userfiles/Factory2/20161124/00b08ec3ec38409a92f32ed692dca388.png
     * Price : 50.0
     * ProductAmount : 50.0
     * ProductName : 测试商品123
     * Integral : 0
     * SalesIntegral : 0
     * PayableIntegral : 0
     * DeductionIntegral : 0.0
     * IsGift : 0
     * Score : 0
     */

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "OrderId")
    public int OrderId;

    @Expose
    @Column(name = "ParentId")
    public int ParentId;

    @Expose
    @Column(name = "GoodsId")
    public int GoodsId;

    @Expose
    @Column(name = "ProductId")
    public int ProductId;

    @Expose
    @Column(name = "Name")
    public String Name;

    @Expose
    @Column(name = "GoodsCode")
    public String GoodsCode;

    @Expose
    @Column(name = "SpecValue")
    public String SpecValue;

    @Expose
    @Column(name = "Quantity")
    public double Quantity;

    @Expose
    @Column(name = "Weight")
    public double Weight;

    @Expose
    @Column(name = "DefaultPic")
    public String DefaultPic;

    @Expose
    @Column(name = "Price")
    public double Price;

    @Expose
    @Column(name = "ProductAmount")
    public double ProductAmount;

    @Expose
    @Column(name = "ProductName")
    public String ProductName;

    @Expose
    @Column(name = "Integral")
    public int Integral;

    @Expose
    @Column(name = "SalesIntegral")
    public int SalesIntegral;

    @Expose
    @Column(name = "PayableIntegral")
    public int PayableIntegral;

    @Expose
    @Column(name = "DeductionIntegral")
    public double DeductionIntegral;

    @Expose
    @Column(name = "IsGift")
    public int IsGift;

    @Expose
    @Column(name = "Score")
    public String Score;

    public OrderItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.OrderId);
        dest.writeInt(this.ParentId);
        dest.writeInt(this.GoodsId);
        dest.writeInt(this.ProductId);
        dest.writeString(this.Name);
        dest.writeString(this.GoodsCode);
        dest.writeString(this.SpecValue);
        dest.writeDouble(this.Quantity);
        dest.writeDouble(this.Weight);
        dest.writeString(this.DefaultPic);
        dest.writeDouble(this.Price);
        dest.writeDouble(this.ProductAmount);
        dest.writeString(this.ProductName);
        dest.writeInt(this.Integral);
        dest.writeInt(this.SalesIntegral);
        dest.writeInt(this.PayableIntegral);
        dest.writeDouble(this.DeductionIntegral);
        dest.writeInt(this.IsGift);
        dest.writeString(this.Score);
    }

    protected OrderItem(Parcel in) {
        this.id = in.readInt();
        this.OrderId = in.readInt();
        this.ParentId = in.readInt();
        this.GoodsId = in.readInt();
        this.ProductId = in.readInt();
        this.Name = in.readString();
        this.GoodsCode = in.readString();
        this.SpecValue = in.readString();
        this.Quantity = in.readDouble();
        this.Weight = in.readDouble();
        this.DefaultPic = in.readString();
        this.Price = in.readDouble();
        this.ProductAmount = in.readDouble();
        this.ProductName = in.readString();
        this.Integral = in.readInt();
        this.SalesIntegral = in.readInt();
        this.PayableIntegral = in.readInt();
        this.DeductionIntegral = in.readDouble();
        this.IsGift = in.readInt();
        this.Score = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
