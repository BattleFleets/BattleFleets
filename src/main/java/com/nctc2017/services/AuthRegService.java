package com.nctc2017.services;

import com.nctc2017.bean.Player;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.VerificationToken;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.TokenDao;
import com.nctc2017.exception.PlayerValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service("authRegService")
public class AuthRegService implements UserDetailsService {
    private static final String NOT_VALID_LOGIN =
            "Username is invalid!\nPlease, choose username in 3-20 length range and use only letters, numbers and underscore.";
    private static final String NOT_VALID_PASS = "Password is invalid! \nPlease, choose password in 8-20 length range.";
    private static final String NOT_MATCHING_PASS = "Passwords are not matching!";
    private static final String NOT_VALID_EMAIL = "Email is invalid! Must look like example@example.com";
    private static final String INCORRECT_AUTH_DATA = "Your login or password are incorrect";

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private ShipDao shipDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Player registration(String login, String password, String passwordConfirm, String email) throws PlayerValidationException {
        if (!isLoginValid(login)) {
            throw new PlayerValidationException(NOT_VALID_LOGIN);
        }
        if (!isEmailValid(email)) {
            throw new PlayerValidationException(NOT_VALID_EMAIL);
        }
        if (!isPasswordValid(password)) {
            throw new PlayerValidationException(NOT_VALID_PASS);
        }
        if (!isPasswordMatching(password, passwordConfirm)) {
            throw new PlayerValidationException(NOT_MATCHING_PASS);
        }

        String playerRegistrationResult = playerDao.addNewPlayer(login.trim(), passwordEncoder.encode(password.trim()), email.trim());
        if (playerRegistrationResult != null) {
            throw new PlayerValidationException(playerRegistrationResult);
        }
        shipDao.createNewShip(DatabaseObject.T_CARАССА_OBJECT_ID,
                playerDao.findPlayerByLogin(login).getPlayerId());
        return playerDao.findPlayerByLogin(login);
    }

    public void confirmRegistration(BigInteger playerId) {
        playerDao.setAccountEnabled(playerId);
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
                throw new PlayerValidationException(INCORRECT_AUTH_DATA);
            }
        } catch (IllegalArgumentException e) {
            throw new PlayerValidationException(INCORRECT_AUTH_DATA);
        }
    }

    public String createVerificationToken(Player player) {
        TokenUtils utils = new TokenUtils();
        String token = utils.generateToken();
        tokenDao.createToken(token, utils.getExpiryDate(), player.getPlayerId());
        return token;
    }

    public String exit(String login) {
        return "Goodbye, " + login + "!";
    }

    private boolean isLoginValid(String login) {
        return login != null && login.matches("(^(\\w|\\d|_){3,20}$)");
    }

    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        int passwordLength = password.trim().length();
        return passwordLength >= 8 && passwordLength <= 20;
    }

    private boolean isPasswordMatching(String password, String passwordConfirm) {
        if (password == null || passwordConfirm == null) {
            return false;
        }
        return password.equals(passwordConfirm.trim());

    }

    private boolean isEmailValid(String email) {
        return email != null && email.matches("(^.+@.+\\..+$)");
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            BigInteger playerId = playerDao.findPlayerByLogin(login).getPlayerId();
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new PlayerUserDetails(playerId,
                    login,
                    playerDao.getPlayerPassword(playerId),
                    playerDao.isAccountEnabled(playerId),
                    true,
                    true,
                    true,
                    authorities);

        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public VerificationToken getVerificationToken(String token) {
        return tokenDao.getToken(token);
    }

    private class TokenUtils {
        private static final long EXPIRATION_DELAY = 86400000L;

        private long getExpiryDate() {
            return new Date().getTime() + EXPIRATION_DELAY;
        }

        private String generateToken() {
            return UUID.randomUUID().toString();
        }
    }


}