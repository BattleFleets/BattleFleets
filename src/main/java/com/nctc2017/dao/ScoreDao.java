package com.nctc2017.dao;

import java.math.BigInteger;

public interface ScoreDao {

    int getScoreForDestroy();

    int getScoreForBoarding();

    int getScoreForSurrender();

    int getScoreForPayoff();
}
