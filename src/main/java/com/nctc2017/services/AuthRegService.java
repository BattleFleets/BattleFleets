package com.nctc2017.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nctc2017.dao.PlayerDao;

@Service("authRegService")
public class AuthRegService {
	@Autowired
	PlayerDao playerDao;

    public String registration(String login, String password, String email) {
        // TODO implement here
        return "";
    }

    public String autorization(String login, String password) {
    	//playerDao.findPlayerByLogin(login);
        return playerDao.findPlayer(43).getLogin();
    }

    public void exit(String login) {
        // TODO implement here
    }

}