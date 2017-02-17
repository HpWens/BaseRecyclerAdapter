package com.example.ecrbtb.mvp.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/14.
 */
@Table(name = "Store")
public class Store implements Parcelable {


    /**
     * Id : 25
     * UserId : 0
     * ZoneId : 35
     * Code : 2
     * Name : 万千百货直营店
     * Nick : 万千百货
     * Logo : /userfiles/Store25/20161213/cbef41d08bf749f4a19daf28750756b2.png
     * LevelId : 9
     * TypeId : -1
     * Intro :
     * Passed : 1
     * Responsible : 李先生
     * Tel : 18113160630
     * Industry : 全部
     * Province : 四川
     * City : 成都
     * Area : 高新区
     * Address : 地方水电费水电费水电费是
     * AddTime : 2016/9/7 19:24:01
     * BeginTime : 2016/9/7 0:00:00
     * ExprieTime : 2017/9/7 0:00:00
     * IsTrial : False
     * Annuity : 100.0000
     * SN : 53617-69336-6E21M-CK432-I3MMM
     * Status : 1
     */

    @Expose
    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "StoreId")
    public String Id;

    @Expose
    @Column(name = "UserId")
    public String UserId;

    @Expose
    @Column(name = "ZoneId")
    public String ZoneId;

    @Expose
    @Column(name = "Code")
    public String Code;

    @Expose
    @Column(name = "Name")
    public String Name;

    @Expose
    @Column(name = "Nick")
    public String Nick;

    @Expose
    @Column(name = "Logo")
    public String Logo;

    @Expose
    @Column(name = "LevelId")
    public String LevelId;

    @Expose
    @Column(name = "TypeId")
    public String TypeId;

    @Expose
    @Column(name = "Intro")
    public String Intro;

    @Expose
    @Column(name = "Passed")
    public String Passed;

    @Expose
    @Column(name = "Responsible")
    public String Responsible;

    @Expose
    @Column(name = "Tel")
    public String Tel;

    @Expose
    @Column(name = "Industry")
    public String Industry;

    @Expose
    @Column(name = "Province")
    public String Province;

    @Expose
    @Column(name = "City")
    public String City;

    @Expose
    @Column(name = "Area")
    public String Area;

    @Expose
    @Column(name = "Address")
    public String Address;

    @Expose
    @Column(name = "AddTime")
    public String AddTime;

    @Expose
    @Column(name = "BeginTime")
    public String BeginTime;

    @Expose
    @Column(name = "ExprieTime")
    public String ExprieTime;

    @Expose
    @Column(name = "IsTrial")
    public String IsTrial;

    @Expose
    @Column(name = "Annuity")
    public String Annuity;

    @Expose
    @Column(name = "SN")
    public String SN;

    @Expose
    @Column(name = "Status")
    public String Status;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.Id);
        dest.writeString(this.UserId);
        dest.writeString(this.ZoneId);
        dest.writeString(this.Code);
        dest.writeString(this.Name);
        dest.writeString(this.Nick);
        dest.writeString(this.Logo);
        dest.writeString(this.LevelId);
        dest.writeString(this.TypeId);
        dest.writeString(this.Intro);
        dest.writeString(this.Passed);
        dest.writeString(this.Responsible);
        dest.writeString(this.Tel);
        dest.writeString(this.Industry);
        dest.writeString(this.Province);
        dest.writeString(this.City);
        dest.writeString(this.Area);
        dest.writeString(this.Address);
        dest.writeString(this.AddTime);
        dest.writeString(this.BeginTime);
        dest.writeString(this.ExprieTime);
        dest.writeString(this.IsTrial);
        dest.writeString(this.Annuity);
        dest.writeString(this.SN);
        dest.writeString(this.Status);
    }

    public Store() {
    }

    protected Store(Parcel in) {
        this.id = in.readInt();
        this.Id = in.readString();
        this.UserId = in.readString();
        this.ZoneId = in.readString();
        this.Code = in.readString();
        this.Name = in.readString();
        this.Nick = in.readString();
        this.Logo = in.readString();
        this.LevelId = in.readString();
        this.TypeId = in.readString();
        this.Intro = in.readString();
        this.Passed = in.readString();
        this.Responsible = in.readString();
        this.Tel = in.readString();
        this.Industry = in.readString();
        this.Province = in.readString();
        this.City = in.readString();
        this.Area = in.readString();
        this.Address = in.readString();
        this.AddTime = in.readString();
        this.BeginTime = in.readString();
        this.ExprieTime = in.readString();
        this.IsTrial = in.readString();
        this.Annuity = in.readString();
        this.SN = in.readString();
        this.Status = in.readString();
    }

    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
