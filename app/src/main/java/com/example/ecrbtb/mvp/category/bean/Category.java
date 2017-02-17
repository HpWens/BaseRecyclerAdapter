package com.example.ecrbtb.mvp.category.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/15.
 */
@Table(name = "Category")
public class Category implements Parcelable {

    /**
     * Id : 837
     * CateName : 手机、数码、电脑办公
     * Spell : ShouJi、ShuMa、DianNaoBanGong
     * Description : null
     * ParentId : 0
     * ParentChain :
     * Layer : 1
     * TypeId : 0
     * OrderId : 1
     * Url : null
     * Icon : /userfiles/20161212/eb9ae4e990a44788909f638ce81a5afc.png
     * ClassName : null
     * Config : null
     * CateTmpId : null
     * DetailTmpId : null
     * UpdateTime : 2016/12/15 9:36:07
     * Status : 1
     * Remark :
     * Name : 手机、数码、电脑办公
     * isChild : false
     */

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "CategoryId")
    public int Id;

    @Expose
    @Column(name = "CateName")
    public String CateName;

    @Expose
    @Column(name = "Spell")
    public String Spell;

    @Expose
    @Column(name = "Description")
    public String Description;

    @Expose
    @Column(name = "ParentId")
    public int ParentId;

    @Expose
    @Column(name = "ParentChain")
    public String ParentChain;

    @Expose
    @Column(name = "Layer")
    public int Layer;

    @Expose
    @Column(name = "TypeId")
    public int TypeId;

    @Expose
    @Column(name = "OrderId")
    public int OrderId;

    @Expose
    @Column(name = "Url")
    public String Url;

    @Expose
    @Column(name = "Icon")
    public String Icon;

    @Expose
    @Column(name = "ClassName")
    public String ClassName;

    @Expose
    @Column(name = "Config")
    public String Config;

    @Expose
    @Column(name = "CateTmpId")
    public String CateTmpId;

    @Expose
    @Column(name = "DetailTmpId")
    public String DetailTmpId;

    @Expose
    @Column(name = "UpdateTime")
    public String UpdateTime;

    @Expose
    @Column(name = "Status")
    public int Status;

    @Expose
    @Column(name = "Remark")
    public String Remark;

    @Expose
    @Column(name = "Name")
    public String Name;

    @Expose
    @Column(name = "isChild")
    public boolean isChild;


    public Category() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.Id);
        dest.writeString(this.CateName);
        dest.writeString(this.Spell);
        dest.writeString(this.Description);
        dest.writeInt(this.ParentId);
        dest.writeString(this.ParentChain);
        dest.writeInt(this.Layer);
        dest.writeInt(this.TypeId);
        dest.writeInt(this.OrderId);
        dest.writeString(this.Url);
        dest.writeString(this.Icon);
        dest.writeString(this.ClassName);
        dest.writeString(this.Config);
        dest.writeString(this.CateTmpId);
        dest.writeString(this.DetailTmpId);
        dest.writeString(this.UpdateTime);
        dest.writeInt(this.Status);
        dest.writeString(this.Remark);
        dest.writeString(this.Name);
        dest.writeByte(this.isChild ? (byte) 1 : (byte) 0);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.Id = in.readInt();
        this.CateName = in.readString();
        this.Spell = in.readString();
        this.Description = in.readString();
        this.ParentId = in.readInt();
        this.ParentChain = in.readString();
        this.Layer = in.readInt();
        this.TypeId = in.readInt();
        this.OrderId = in.readInt();
        this.Url = in.readString();
        this.Icon = in.readString();
        this.ClassName = in.readString();
        this.Config = in.readString();
        this.CateTmpId = in.readString();
        this.DetailTmpId = in.readString();
        this.UpdateTime = in.readString();
        this.Status = in.readInt();
        this.Remark = in.readString();
        this.Name = in.readString();
        this.isChild = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
