package com.nctc2017.bean;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.*;

public class Market {

    private static final int INITIAL_CAPACITY = 21;

    protected Map<BigInteger, GoodsForSale> bar;

    public Market(List<GoodsForSale> goodsForSales) {
        bar = new HashMap<>(INITIAL_CAPACITY);

        for (GoodsForSale goods : goodsForSales) {
            if (goods != null) {
                bar.put(goods.getTemplateId(), goods);
            }
        }
    }

    public Market(Market market) {
        bar = new HashMap<>(INITIAL_CAPACITY);
        for (Map.Entry<BigInteger, GoodsForSale> entry : market.bar.entrySet()){
            bar.put(entry.getKey(), new GoodsForSale(entry.getValue()));
        }
    }

    public int getGoodsQuantity(BigInteger id) {
        return bar.get(id).getQuantity();
    }

    public GoodsForSale.GoodsType getGoodsType(BigInteger id) {
        return bar.get(id).getType();
    }

    public int getGoodsBuyingPrice(BigInteger id) {
        return bar.get(id).getBuyingPrice();
    }

    public int getGoodsSalePrice(BigInteger id) {
        return bar.get(id).getSalePrice();
    }

    public GoodsForSale getGoods(BigInteger id) {
        return bar.get(id);
    }

    public void updateGoodsQuantity(BigInteger id, int quantity) {
        GoodsForSale goods = bar.get(id);
        goods.setQuantity(quantity);
    }

    public void updateGoodsBuyingPrice(BigInteger id, int newGoodsPrice) {
        bar.get(id).setBuyingPrice(newGoodsPrice);
    }

    public void updateGoodsSalePrice(BigInteger id, int newGoodsPrice) {
        bar.get(id).setSalePrice(newGoodsPrice);
    }

    public Set<BigInteger> getAllGoodsIds() {
        return bar.keySet();
    }

    public List<GoodsForSale> getAllGoodsValues() {return new ArrayList<>(bar.values());}

    public Map<BigInteger, GoodsForSale> getAllGoods(){
        return bar;
    }

}