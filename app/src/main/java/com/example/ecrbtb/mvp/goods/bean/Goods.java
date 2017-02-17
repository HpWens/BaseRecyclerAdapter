package com.example.ecrbtb.mvp.goods.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/19.
 */
@Table(name = "Goods")
public class Goods implements Parcelable {


    /**
     * ProductId : 898
     * Id : 1985
     * GoodsCode : C8ZO1612160950556718404_00
     * Name : 韩束爽肤水女墨菊巨补水秋冬深层保湿收缩毛孔化妆护肤柔肤水正品[50mL]
     * SalesPrice : 59.0
     * SalesIntegral : 0
     * SpecValueIds : <SpecValue Id="99" SpecId="24" SpecName="容量" ShowType="文字"><![CDATA[50mL]]></SpecValue>
     * DefaultPic : /userfiles/Supplier88/20161216/8fb3596d7eff4798b1adcefdc288d625.jpg
     * Stock : 100.0
     * SupplierId : 88
     * Shelved : 1
     * Sells : 0.0
     * Cost : 30.0
     * BarCode : hs001
     * Weight : 0.0
     * ProductName : 韩束爽肤水女墨菊巨补水秋冬深层保湿收缩毛孔化妆护肤柔肤水正品
     * SaleSpecValueMode : 0
     * Unit : 瓶
     * CartCount : 3
     * PriceRules : [{"MinQuantity":1,"MaxQuantity":2.147483647E9,"Price":59}]
     * : 50mL
     * Specs : [{"Id":"99","SpecId":"24","SpecName":"容量","ShowType":"文字","SpecValue_Text":"50mL"}]
     * PriceType : 0
     * MinQuantity : 1.0
     * Promotions : []
     */

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "ProductId")
    public int ProductId;

    @Expose
    @Column(name = "GoodsId")
    public int Id;

    @Expose
    @Column(name = "GoodsCode")
    public String GoodsCode;

    @Expose
    @Column(name = "Name")
    public String Name;

    @Expose
    @Column(name = "SalesPrice")
    public double SalesPrice;

    @Expose
    @Column(name = "SalesIntegral")
    public double SalesIntegral;

    @Expose
    @Column(name = "SpecValueIds")
    public String SpecValueIds;

    @Expose
    @Column(name = "DefaultPic")
    public String DefaultPic;

    @Expose
    @Column(name = "Stock")
    public double Stock;

    @Expose
    @Column(name = "SupplierId")
    public int SupplierId;

    @Expose
    @Column(name = "Shelved")
    public int Shelved;

    @Expose
    @Column(name = "Sells")
    public double Sells;

    @Expose
    @Column(name = "Cost")
    public double Cost;

    @Expose
    @Column(name = "BarCode")
    public String BarCode;

    @Expose
    @Column(name = "Weight")
    public double Weight;

    @Expose
    @Column(name = "ProductName")
    public String ProductName;

    @Expose
    @Column(name = "SaleSpecValueMode")
    public int SaleSpecValueMode;

    @Expose
    @Column(name = "Unit")
    public String Unit;

    @Expose
    @Column(name = "CartCount")
    public int CartCount;

    @Expose
    @Column(name = "SaleMode")
    public int SaleMode;

    @Expose
    @Column(name = "SpecValue")
    public String SpecValue;

    @Column(name = "GoodsNumber")
    public int GoodsNumber;

    @Column(name = "ActualUnitPrice")
    public double ActualUnitPrice;

    @Column(name = "AddCartTime")
    public long AddCartTime;

    @Expose
    @Column(name = "Deductrate")
    public float Deductrate;

    public String SupplierName;

    @Expose
    public double Price;

    //运费
    public String Freight;

    public Goods() {
        SpecValue = "";
        SaleMode = 0;
        Deductrate = 1;
        SupplierName = "";
        Price = 0;
        SalesIntegral = 0;
        Freight = "0.00";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.ProductId);
        dest.writeInt(this.Id);
        dest.writeString(this.GoodsCode);
        dest.writeString(this.Name);
        dest.writeDouble(this.SalesPrice);
        dest.writeDouble(this.SalesIntegral);
        dest.writeString(this.SpecValueIds);
        dest.writeString(this.DefaultPic);
        dest.writeDouble(this.Stock);
        dest.writeInt(this.SupplierId);
        dest.writeInt(this.Shelved);
        dest.writeDouble(this.Sells);
        dest.writeDouble(this.Cost);
        dest.writeString(this.BarCode);
        dest.writeDouble(this.Weight);
        dest.writeString(this.ProductName);
        dest.writeInt(this.SaleSpecValueMode);
        dest.writeString(this.Unit);
        dest.writeInt(this.CartCount);
        dest.writeInt(this.SaleMode);
        dest.writeString(this.SpecValue);
        dest.writeInt(this.GoodsNumber);
        dest.writeDouble(this.ActualUnitPrice);
        dest.writeLong(this.AddCartTime);
        dest.writeFloat(this.Deductrate);
        dest.writeString(this.SupplierName);
        dest.writeDouble(this.Price);
        dest.writeString(this.Freight);
    }

    protected Goods(Parcel in) {
        this.id = in.readInt();
        this.ProductId = in.readInt();
        this.Id = in.readInt();
        this.GoodsCode = in.readString();
        this.Name = in.readString();
        this.SalesPrice = in.readDouble();
        this.SalesIntegral = in.readDouble();
        this.SpecValueIds = in.readString();
        this.DefaultPic = in.readString();
        this.Stock = in.readDouble();
        this.SupplierId = in.readInt();
        this.Shelved = in.readInt();
        this.Sells = in.readDouble();
        this.Cost = in.readDouble();
        this.BarCode = in.readString();
        this.Weight = in.readDouble();
        this.ProductName = in.readString();
        this.SaleSpecValueMode = in.readInt();
        this.Unit = in.readString();
        this.CartCount = in.readInt();
        this.SaleMode = in.readInt();
        this.SpecValue = in.readString();
        this.GoodsNumber = in.readInt();
        this.ActualUnitPrice = in.readDouble();
        this.AddCartTime = in.readLong();
        this.Deductrate = in.readFloat();
        this.SupplierName = in.readString();
        this.Price = in.readDouble();
        this.Freight = in.readString();
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
