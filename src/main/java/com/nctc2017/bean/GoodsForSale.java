package com.nctc2017.bean;

import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigInteger;

public class GoodsForSale {

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
    protected GoodsForBuying.GoodsType type;

    @JsonView(View.Sell.class)
    protected int salePrice;


    public GoodsForSale(BigInteger goodsId, BigInteger goodsTemplateId, int quantity, GoodsForBuying.GoodsType type) {
        this.goodsId = goodsId;
        this.goodsTemplateId = goodsTemplateId;
        this.quantity = quantity;
        this.type = type;
        name = "";
        description = "";
    }

    public GoodsForSale setName(String name) {
        this.name = name;
        return this;
    }

    public GoodsForSale setSalePrice(int salePrice) {
        this.salePrice = salePrice;
        return this;
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

    public GoodsForBuying.GoodsType getType() {
        return type;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public GoodsForSale appendDescription(String description) {
        description = description.trim();
        if(description.isEmpty()) return this;
        description = description.substring(0, 1).toUpperCase() + description.substring(1);
        description = description.concat(". ");
        this.description = this.description + description;
        return this;
    }

    @Override
    public String toString() {
        return "GoodsForSale{" +
                "goodsId=" + goodsId +
                ", goodsTemplateId=" + goodsTemplateId +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", salePrice=" + salePrice +
                '}';
    }
}
