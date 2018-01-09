package com.nctc2017.bean;

import java.math.BigInteger;

public class PlayerGoodsForSale {

    protected BigInteger goodsId;
    protected BigInteger goodsTemplateId;
    protected int quantity;
    protected String name;
    protected String description;
    protected GoodsForSale.GoodsType type;
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
