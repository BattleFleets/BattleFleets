package com.nctc2017.services;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Goods;

@Service
@Transactional
public class BattleEndingService {

    public List<Goods> passBoardGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    public List<Goods> passDestroyGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    public boolean destroyShip(int idShip) {
        // TODO implement here
        return false;
    }

    public List<Goods> passSurrenderGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    public int passPayOffGoodsToWinner(int idPlayer) {
        // TODO implement here
        return 0;
    }

}