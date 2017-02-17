package com.example.ecrbtb.mvp.login.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/9.
 */
@Table(name = "user")
public class User {

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @SerializedName("userName")
    @Column(name = "userName")
    private String userName;

    @Expose
    @Column(name = "passWord")
    private String passWord;

    @Expose
    @Column(name = "storeCode")
    private String storeCode;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", storeCode='" + storeCode + '\'' +
                '}';
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
