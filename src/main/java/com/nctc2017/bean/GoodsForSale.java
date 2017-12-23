package com.nctc2017.bean;

import java.math.BigInteger;

public class GoodsForSale {

    protected final BigInteger templateId;
    protected final String name;
    protected final String goodsDescription;
    protected final GoodsType type;
    protected int goodsRarity;
    protected volatile int buyingPrice;
    protected volatile int quantity;
    protected volatile int salePrice;

    public enum GoodsType{
        GOODS, AMMO, MAST, CANNON
    }

    public GoodsForSale(BigInteger templateId, String name, String goodsDescription, GoodsType type) {
        this.templateId = templateId;
        this.name = name;
        this.goodsDescription = goodsDescription;
        this.type = type;
    }

    public GoodsForSale(GoodsForSale origin){
        this(origin.templateId, origin.name, origin.goodsDescription, origin.type);
    }

    public String getName() {
        return name;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getGoodsRarity() {
        return goodsRarity;
    }

    public void setGoodsRarity(int goodsRarity) {
        this.goodsRarity = goodsRarity;
    }

    public BigInteger getTemplateId() {
        return templateId;
    }

    public GoodsType getType() {
        return type;
    }
}