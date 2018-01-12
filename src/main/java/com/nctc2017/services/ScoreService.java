package com.nctc2017.services;

import com.nctc2017.dao.ScoreDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ScoreService {
    @Autowired
    private ScoreDao scoreDao;

    public int getScoreForDestroy() {
        return scoreDao.getScoreForDestroy();

    }

    public int getScoreForBoarding() {
        return scoreDao.getScoreForBoarding();
    }

    public int getScoreForSurrender() {
        return scoreDao.getScoreForSurrender();
    }

    public int getScoreForPayoff() {
        return scoreDao.getScoreForPayoff();
    }
}
