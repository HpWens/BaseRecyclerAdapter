package com.example.ecrbtb.mvp.goods.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/19.
 */
@Table(name = "PriceRules")
public class PriceRules implements Parcelable {


    /**
     * MinQuantity : 1.0
     * MaxQuantity : 2.147483647E9
     * Price : 59.0
     */

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "ProductId")
    public int ProductId;

    @Column(name = "GoodsId")
    public int GoodsId;

    @Expose
    @Column(name = "PriceRulesId")
    public int Id;

    @Expose
    @Column(name = "MinQuantity")
    public double MinQuantity;

    @Expose
    @Column(name = "MaxQuantity")
    public double MaxQuantity;

    @Expose
    @Column(name = "Price")
    public double Price;

    @Column(name = "Unit")
    public String Unit;

    @Column(name = "SupplierId")
    public int SupplierId;

    @Expose
    @Column(name = "Integral")
    public double Integral;


    public PriceRules() {
        Id = 0;
        Integral = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.ProductId);
        dest.writeInt(this.GoodsId);
        dest.writeInt(this.Id);
        dest.writeDouble(this.MinQuantity);
        dest.writeDouble(this.MaxQuantity);
        dest.writeDouble(this.Price);
        dest.writeString(this.Unit);
        dest.writeInt(this.SupplierId);
    }

    protected PriceRules(Parcel in) {
        this.id = in.readInt();
        this.ProductId = in.readInt();
        this.GoodsId = in.readInt();
        this.Id = in.readInt();
        this.MinQuantity = in.readDouble();
        this.MaxQuantity = in.readDouble();
        this.Price = in.readDouble();
        this.Unit = in.readString();
        this.SupplierId = in.readInt();
    }

    public static final Creator<PriceRules> CREATOR = new Creator<PriceRules>() {
        @Override
        public PriceRules createFromParcel(Parcel source) {
            return new PriceRules(source);
        }

        @Override
        public PriceRules[] newArray(int size) {
            return new PriceRules[size];
        }
    };
}
