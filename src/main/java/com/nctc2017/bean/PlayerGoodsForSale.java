package com.nctc2017.bean;

import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigInteger;

public class PlayerGoodsForSale {

    @JsonView(View.Sell.class)
    protected BigInteger goodsId;

    @JsonView(View.Sell.class)
    protected BigInteger goodsTemplateId;

    @JsonView(View.No.class)
    protected int quantity;

    @JsonView(View.No.class)
    protected String name;

    @JsonView(View.No.class)
    protected String description;

    @JsonView(View.Sell.class)
    protected GoodsForSale.GoodsType type;

    @JsonView(View.Sell.class)
    protected int salePrice;


    public PlayerGoodsForSale(BigInteger goodsId, BigInteger goodsTemplateId, int quantity, GoodsForSale.GoodsType type) {
        this.goodsId = goodsId;
        this.goodsTemplateId = goodsTemplateId;
        this.quantity = quantity;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public BigInteger getGoodsId() {
        return goodsId;
    }

    public BigInteger getGoodsTemplateId() {
        return goodsTemplateId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GoodsForSale.GoodsType getType() {
        return type;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void appendDescription(String description) {
        this.description = this.description + description;
    }


}
