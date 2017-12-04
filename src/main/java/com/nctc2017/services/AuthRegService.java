package com.nctc2017.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nctc2017.dao.PlayerDao;

/**
 * 
 */
@Service("authRegService")
public class AuthRegService {
	@Autowired
	PlayerDao playerDao;

    /**
     * Default constructor
     */
    public AuthRegService() {
    }

    /**
     * @param String login 
     * @param String password 
     * @param String email 
     * @return
     */
    public String registration(String login, String password, String email) {
        // TODO implement here
        return "";
    }

    /**
     * @param String login 
     * @param String password 
     * @return
     */
    public String autorization(String login, String password) {
    	//playerDao.findPlayerByLogin(login);
        return playerDao.findPlayer(43).getLogin();
    }

    /**
     * @param String login 
     * @return
     */
    public void exit(String login) {
        // TODO implement here
    }

}