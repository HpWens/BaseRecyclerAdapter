package com.example.ecrbtb.mvp.order.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by boby on 2017/1/6.
 */

public class Address implements Parcelable {


    /**
     * Id : 58
     * Name : 测试
     * Province : 北京
     * City : 北京市辖
     * Area : 东城区
     * Address : 11
     * ZIP :
     * Mobile : 13009650248
     * Tel :
     * IsDefault : 0
     * AddTime : 2016/12/30 17:29:52
     * IsChecked : 0
     */

    @Expose
    public int Id;

    @Expose
    public String Name;

    @Expose
    public String Province;

    @Expose
    public String City;

    @Expose
    public String Area;

    @Expose
    public String Address;

    @Expose
    public String ZIP;

    @Expose
    public String Mobile;

    @Expose
    public String Tel;

    @Expose
    public int IsDefault;

    @Expose
    public String AddTime;

    public boolean IsChecked;


    public Address() {
        Id = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.Name);
        dest.writeString(this.Province);
        dest.writeString(this.City);
        dest.writeString(this.Area);
        dest.writeString(this.Address);
        dest.writeString(this.ZIP);
        dest.writeString(this.Mobile);
        dest.writeString(this.Tel);
        dest.writeInt(this.IsDefault);
        dest.writeString(this.AddTime);
        dest.writeByte(this.IsChecked ? (byte) 1 : (byte) 0);
    }

    protected Address(Parcel in) {
        this.Id = in.readInt();
        this.Name = in.readString();
        this.Province = in.readString();
        this.City = in.readString();
        this.Area = in.readString();
        this.Address = in.readString();
        this.ZIP = in.readString();
        this.Mobile = in.readString();
        this.Tel = in.readString();
        this.IsDefault = in.readInt();
        this.AddTime = in.readString();
        this.IsChecked = in.readByte() != 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
