package com.nctc2017.exception;

public class BattleEndException extends Exception{

    public BattleEndException() {
        super();
    }

    public BattleEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public BattleEndException(String message) {
        super(message);
    }

    public BattleEndException(Throwable cause) {
        super(cause);
    }
    
}
