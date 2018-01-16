package com.nctc2017.services.utils.endVisitorImpl;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.services.utils.BattleEndVisitor;

@Component
public class PayoffDefaultBattleEnd implements BattleEndVisitor {
    private static final Logger LOG = Logger.getLogger(PayoffDefaultBattleEnd.class);

    @Override
    public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
            BigInteger winnerId, BigInteger loserId) {
        LOG.debug("Player_" + loserId + " pass money to winner Player_" + winnerId);
    }

}
