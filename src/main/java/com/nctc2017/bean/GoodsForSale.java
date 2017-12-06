package com.nctc2017.bean;
public class GoodsForSale {
    protected String name;

    protected int buyingPrice;

    protected int raraty;

    protected int quantity;

    protected int salePrice;

    protected int templateId;
    

	public GoodsForSale(String name, int buyingPrice, int raraty, int quantity, int salePrice, int templateId) {
		this.name = name;
		this.buyingPrice = buyingPrice;
		this.raraty = raraty;
		this.quantity = quantity;
		this.salePrice = salePrice;
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(int buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public int getRaraty() {
		return raraty;
	}

	public void setRaraty(int raraty) {
		this.raraty = raraty;
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

}