package com.example.ecrbtb.mvp.order.bean;

import java.util.List;

/**
 * Created by boby on 2017/1/16.
 */

public class OrderData {

    public String ProductIds;

    //商品信息
    public String ProductInfo;

    //地址信息
    public Address AddressData;

    //支付类型
    public int PayType;

    //支付金额
    public double PayPrice;

    //发票类型
    public int InvoiceType;

    //优惠券集合
    public List<Coupon> Coupons;

    //普通发票类型
    public int CommInvoiceType;

    //普通发票抬头
    public String CommInvoiceRise;

    //普通发票的内容
    public String CommInvoiceContent;

    //增值税发票数据集合
    public String[] IncrementInvoices;

    public OrderData(Builder builder) {
        this.ProductIds = builder.ProductIds;
        this.ProductInfo = builder.ProductInfo;
        this.AddressData = builder.AddressData;
        this.PayType = builder.PayType;
        this.PayPrice = builder.PayPrice;
        this.InvoiceType = builder.InvoiceType;
        this.Coupons = builder.Coupons;
        this.CommInvoiceType = builder.CommInvoiceType;
        this.CommInvoiceRise = builder.CommInvoiceRise;
        this.CommInvoiceContent = builder.CommInvoiceContent;
        this.IncrementInvoices = builder.IncrementInvoices;
    }


    public static class Builder {

        private String ProductIds;

        //商品信息
        private String ProductInfo;

        //地址信息
        private Address AddressData;

        //支付类型
        private int PayType;

        //支付金额
        private double PayPrice;

        //发票类型
        private int InvoiceType;

        //优惠券集合
        private List<Coupon> Coupons;

        //普通发票类型
        private int CommInvoiceType;

        //普通发票抬头
        private String CommInvoiceRise;

        //普通发票的内容
        private String CommInvoiceContent;


        //增值税发票数据集合
        private String[] IncrementInvoices;

        public Builder ProductId(String productIds) {
            this.ProductIds = productIds;
            return this;
        }


        public Builder ProductInfo(String productInfo) {
            this.ProductInfo = productInfo;
            return this;
        }


        public Builder AddressData(Address addressData) {
            this.AddressData = addressData;
            return this;
        }


        public Builder PayType(int payType) {
            this.PayType = payType;
            return this;
        }


        public Builder PayPrice(double payPrice) {
            this.PayPrice = payPrice;
            return this;
        }


        public Builder InvoiceType(int invoiceType) {
            this.InvoiceType = invoiceType;
            return this;
        }


        public Builder Coupons(List<Coupon> coupons) {
            this.Coupons = coupons;
            return this;
        }


        public Builder CommInvoiceType(int commInvoiceType) {
            this.CommInvoiceType = commInvoiceType;
            return this;
        }


        public Builder CommInvoiceRise(String commInvoiceRise) {
            this.CommInvoiceRise = commInvoiceRise;
            return this;
        }


        public Builder CommInvoiceContent(String commInvoiceContent) {
            this.CommInvoiceContent = commInvoiceContent;
            return this;
        }


        public Builder IncrementInvoices(String[] incrementInvoices) {
            this.IncrementInvoices = incrementInvoices;
            return this;
        }

        public OrderData build() {
            return new OrderData(this);
        }

    }

}
