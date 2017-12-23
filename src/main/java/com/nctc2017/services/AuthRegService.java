package com.nctc2017.services;

import com.nctc2017.bean.Player;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.exception.PlayerValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service("authRegService")
public class AuthRegService {
    private static final String NOT_VALID_LOGIN = "Login is not valid! Must be between 3 and 20 characters!";
    private static final String NOT_VALID_PASS = "Password is not valid! Must be between 8 and 20 characters!";

    @Autowired
    PlayerDao playerDao;

    public Player registration(String login, String password, @Email String email) throws PlayerValidationException {
        if (!isLoginValid(login)) {
            throw new PlayerValidationException(NOT_VALID_LOGIN);
        }
        if (!isPasswordValid(password)) {
            throw new PlayerValidationException(NOT_VALID_PASS);
        }
        String playerRegistrationResult = playerDao.addNewPlayer(login.trim(), String.valueOf(password.trim().hashCode()), email.trim());
        if (!playerRegistrationResult.isEmpty()) {
            throw new PlayerValidationException(playerRegistrationResult);
        }
        return playerDao.findPlayerByLogin(login);
    }

    public Player authorization(String login, String password) throws PlayerValidationException {
        if (!isLoginValid(login)) {
            throw new PlayerValidationException(NOT_VALID_LOGIN);
        }
        if (isPasswordValid(password)) {
            throw new PlayerValidationException(NOT_VALID_PASS);
        }
        try {
            Player player = playerDao.findPlayerByLogin(login);
            String realPlayerPassword = playerDao.getPlayerPassword(player.getPlayerId());
            if (String.valueOf(password.trim().hashCode()).equals(realPlayerPassword)) {
                return player;
            } else {
                throw new PlayerValidationException("Incorrect password. Try again");
            }
        } catch (IllegalArgumentException e) {
            throw new PlayerValidationException("Incorrect login. Try again");
        }
    }

    public String exit(String login) {
        return "Goodbye, " + login + "!";
    }

    private boolean isLoginValid(String login) {
        if (login == null) {
            return false;
        }
        int loginLength = login.trim().length();
        return loginLength >= 3 && loginLength <= 20;
    }

    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        int passwordLength = password.trim().length();
        return passwordLength >= 8 && passwordLength <= 20;

    }

}