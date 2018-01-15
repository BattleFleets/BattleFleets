package com.nctc2017.bean;

import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

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
        this.name = "";
        this.description = "";
    }

    public PlayerGoodsForSale setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerGoodsForSale setSalePrice(int salePrice) {
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

    public GoodsForSale.GoodsType getType() {
        return type;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public PlayerGoodsForSale appendDescription(@NotNull String description) {
        description = description.trim();
        if(description.isEmpty()) return this;
        description = description.substring(0, 1).toUpperCase() + description.substring(1);
        description = description.concat(". ");
        this.description = this.description + description;
        return this;
    }

    @Override
    public String toString() {
        return "PlayerGoodsForSale{" +
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
