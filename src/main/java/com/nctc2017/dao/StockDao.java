package com.nctc2017.dao;

public interface StockDao {

	int findStockId(int player_id);

	int createStock(int playerId);

	void deleteStock(int playerId);

	void addCargo(int cargoId);

}