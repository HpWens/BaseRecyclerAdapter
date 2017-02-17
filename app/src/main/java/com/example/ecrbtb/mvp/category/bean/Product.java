package com.example.ecrbtb.mvp.category.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/16.
 */
@Table(name = "Product")
public class Product implements Parcelable {

    /**
     * Id : 625
     * ProductId : 618
     * StoreId : 32
     * ProductCode : Q5TR1611081945043334382
     * BarCode : 464
     * ProductName : 贝亲（Pigeon）婴儿润肤露（滋润型）
     * CategoryId : 1014
     * CateName : 洗发沐浴
     * CustomCateId : 158
     * CustomCateName : 洗护
     * BrandId : 131
     * BrandName : 贝亲
     * FKId : 0
     * FKFlag : 0
     * SupplierName : 童真童趣专营店
     * Logo : /userfiles/Store32/20161213/37a631b4c90248ba90d486e3e69a0eee.png
     * PriceType : -1
     * Shelved : 1
     * Status : 1
     * Mode : 0
     * Price : 29.0
     * SaleMode : 0
     * SalesPrice : 12.0
     * SalesIntegral : 0.0
     * UpdateTime : 2016/11/9 9:08:19
     * DefaultPic : /userfiles/20161108/115ee23cbd3d4fdbb1f31d6f13bcf0b5.png
     * Marketable : 1
     * AddTime : 2016/12/13 13:50:45
     * ShelveTime : 2016/12/16 12:43:08
     * Sells : 0.0
     * IsSingle : 1
     * ProductGoodsId : 1309
     * CartCount : 0
     * Stock : 100.0
     */

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "PId")
    public int Id;

    @Expose
    @Column(name = "ProductId")
    public int ProductId;

    @Expose
    @Column(name = "StoreId")
    public int StoreId;

    @Expose
    @Column(name = "ProductCode")
    public String ProductCode;

    @Expose
    @Column(name = "BarCode")
    public String BarCode;

    @Expose
    @Column(name = "ProductName")
    public String ProductName;

    @Expose
    @Column(name = "CategoryId")
    public int CategoryId;

    @Expose
    @Column(name = "CateName")
    public String CateName;

    @Expose
    @Column(name = "CustomCateId")
    public String CustomCateId;

    @Expose
    @Column(name = "CustomCateName")
    public String CustomCateName;

    @Expose
    @Column(name = "BrandId")
    public int BrandId;

    @Expose
    @Column(name = "BrandName")
    public String BrandName;

    @Expose
    @Column(name = "FKId")
    public int FKId;

    @Expose
    @Column(name = "FKFlag")
    public int FKFlag;

    @Expose
    @Column(name = "SupplierName")
    public String SupplierName;

    @Expose
    @Column(name = "Logo")
    public String Logo;

    @Expose
    @Column(name = "PriceType")
    public String PriceType;

    @Expose
    @Column(name = "Shelved")
    public int Shelved;

    @Expose
    @Column(name = "Status")
    public String Status;

    @Expose
    @Column(name = "Mode")
    public String Mode;

    @Expose
    @Column(name = "Price")
    public double Price;

    @Expose
    @Column(name = "SaleMode")
    public int SaleMode;

    @Expose
    @Column(name = "SalesPrice")
    public double SalesPrice;

    @Expose
    @Column(name = "SalesIntegral")
    public double SalesIntegral;

    @Expose
    @Column(name = "UpdateTime")
    public String UpdateTime;

    @Expose
    @Column(name = "DefaultPic")
    public String DefaultPic;

    @Expose
    @Column(name = "Marketable")
    public int Marketable;

    @Expose
    @Column(name = "AddTime")
    public String AddTime;

    @Expose
    @Column(name = "ShelveTime")
    public String ShelveTime;

    @Expose
    @Column(name = "Sells")
    public double Sells;

    @Expose
    @Column(name = "IsSingle")
    public String IsSingle;

    @Expose
    @Column(name = "ProductGoodsId")
    public String ProductGoodsId;

    @Expose
    @Column(name = "CartCount")
    public int CartCount;

    @Expose
    @Column(name = "Stock")
    public double Stock;

    @Expose
    @Column(name = "CId")  //关联 categoryid
    public int CId;

    @Expose
    @Column(name = "IsDeduction")
    public String IsDeduction;

    //产品数量
    @Column(name = "ProductNum")
    public int ProductNum;

    @Expose
    @Column(name = "SupplierId")
    public int SupplierId;

    @Column(name = "AddCartTime")
    public long AddCartTime;

    @Expose
    @Column(name = "MinIntegral")
    public double MinIntegral;

    @Expose
    @Column(name = "MinPrice")
    public double MinPrice;

    @Column(name = "GoodsId")
    public int GoodsId;

    // 0 表示钱   1 表示积分  2表示钱+积分  3表示钱可抵积分
    public int CartMode;

    //购物车单价
    public double CartPrice;

    //购物车积分
    public double CartIntegral;

    public boolean IsChecked;

    //抵扣比率
    @Expose
    @Column(name = "Deductrate")
    public float Deductrate;

    @Column(name = "Pics")
    public String Pics;

    @Column(name = "CategoryName")
    public String CategoryName;

    public Product() {
        SupplierId = 0;
        IsDeduction = "0";
        MinPrice = 0.0;
        MinIntegral = 0;
        GoodsId = 0;
        BrandId = 0;
        BrandName = "";
        CartMode = 0;
        CartPrice = 0;
        CartIntegral = 0;
        Deductrate = 1;
        Stock = 0;
        PriceType = "-1";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.Id);
        dest.writeInt(this.ProductId);
        dest.writeInt(this.StoreId);
        dest.writeString(this.ProductCode);
        dest.writeString(this.BarCode);
        dest.writeString(this.ProductName);
        dest.writeInt(this.CategoryId);
        dest.writeString(this.CateName);
        dest.writeString(this.CustomCateId);
        dest.writeString(this.CustomCateName);
        dest.writeInt(this.BrandId);
        dest.writeString(this.BrandName);
        dest.writeInt(this.FKId);
        dest.writeInt(this.FKFlag);
        dest.writeString(this.SupplierName);
        dest.writeString(this.Logo);
        dest.writeString(this.PriceType);
        dest.writeInt(this.Shelved);
        dest.writeString(this.Status);
        dest.writeString(this.Mode);
        dest.writeDouble(this.Price);
        dest.writeInt(this.SaleMode);
        dest.writeDouble(this.SalesPrice);
        dest.writeDouble(this.SalesIntegral);
        dest.writeString(this.UpdateTime);
        dest.writeString(this.DefaultPic);
        dest.writeInt(this.Marketable);
        dest.writeString(this.AddTime);
        dest.writeString(this.ShelveTime);
        dest.writeDouble(this.Sells);
        dest.writeString(this.IsSingle);
        dest.writeString(this.ProductGoodsId);
        dest.writeInt(this.CartCount);
        dest.writeDouble(this.Stock);
        dest.writeInt(this.CId);
        dest.writeString(this.IsDeduction);
        dest.writeInt(this.ProductNum);
        dest.writeInt(this.SupplierId);
        dest.writeLong(this.AddCartTime);
        dest.writeDouble(this.MinIntegral);
        dest.writeDouble(this.MinPrice);
        dest.writeInt(this.GoodsId);
        dest.writeInt(this.CartMode);
        dest.writeDouble(this.CartPrice);
        dest.writeDouble(this.CartIntegral);
        dest.writeByte(this.IsChecked ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.Deductrate);
        dest.writeString(this.Pics);
        dest.writeString(this.CategoryName);
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.Id = in.readInt();
        this.ProductId = in.readInt();
        this.StoreId = in.readInt();
        this.ProductCode = in.readString();
        this.BarCode = in.readString();
        this.ProductName = in.readString();
        this.CategoryId = in.readInt();
        this.CateName = in.readString();
        this.CustomCateId = in.readString();
        this.CustomCateName = in.readString();
        this.BrandId = in.readInt();
        this.BrandName = in.readString();
        this.FKId = in.readInt();
        this.FKFlag = in.readInt();
        this.SupplierName = in.readString();
        this.Logo = in.readString();
        this.PriceType = in.readString();
        this.Shelved = in.readInt();
        this.Status = in.readString();
        this.Mode = in.readString();
        this.Price = in.readDouble();
        this.SaleMode = in.readInt();
        this.SalesPrice = in.readDouble();
        this.SalesIntegral = in.readDouble();
        this.UpdateTime = in.readString();
        this.DefaultPic = in.readString();
        this.Marketable = in.readInt();
        this.AddTime = in.readString();
        this.ShelveTime = in.readString();
        this.Sells = in.readDouble();
        this.IsSingle = in.readString();
        this.ProductGoodsId = in.readString();
        this.CartCount = in.readInt();
        this.Stock = in.readDouble();
        this.CId = in.readInt();
        this.IsDeduction = in.readString();
        this.ProductNum = in.readInt();
        this.SupplierId = in.readInt();
        this.AddCartTime = in.readLong();
        this.MinIntegral = in.readDouble();
        this.MinPrice = in.readDouble();
        this.GoodsId = in.readInt();
        this.CartMode = in.readInt();
        this.CartPrice = in.readDouble();
        this.CartIntegral = in.readDouble();
        this.IsChecked = in.readByte() != 0;
        this.Deductrate = in.readFloat();
        this.Pics = in.readString();
        this.CategoryName = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
