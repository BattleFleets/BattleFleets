package com.nctc2017.bean;

/**
 * 
 */
public class Battle {
    /**
     * 
     */
    protected int distance;

    /**
     * 
     */
    protected int idShip1;

    /**
     * 
     */
    protected int idShip2;

    /**
     * 
     */
    protected int idPlayer1;

    /**
     * 
     */
    protected int idPlayer2;

    /**
     * 
     */
    protected boolean readyPlayer1;

    /**
     * 
     */
    protected boolean readyPlayer2;

    /**
     * 
     */
    protected boolean convergencePlayer1;

    /**
     * 
     */
    protected boolean convergencePlayer2;

    /**
     * Default constructor
     */
    public Battle() {
    }

	public Battle(int distance, int idShip1, int idShip2, int idPlayer1, int idPlayer2) {
		this.distance = distance;
		this.idShip1 = idShip1;
		this.idShip2 = idShip2;
		this.idPlayer1 = idPlayer1;
		this.idPlayer2 = idPlayer2;
		this.readyPlayer1 = false;
		this.convergencePlayer1 = false;
		this.readyPlayer2 = false;
		this.convergencePlayer2 = false;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getIdShip1() {
		return idShip1;
	}

	public void setIdShip1(int idShip1) {
		this.idShip1 = idShip1;
	}

	public int getIdShip2() {
		return idShip2;
	}

	public void setIdShip2(int idShip2) {
		this.idShip2 = idShip2;
	}

	public int getIdPlayer1() {
		return idPlayer1;
	}

	public void setIdPlayer1(int idPlayer1) {
		this.idPlayer1 = idPlayer1;
	}

	public int getIdPlayer2() {
		return idPlayer2;
	}

	public void setIdPlayer2(int idPlayer2) {
		this.idPlayer2 = idPlayer2;
	}

	public boolean isReadyPlayer1() {
		return readyPlayer1;
	}

	public void setReadyPlayer1(boolean readyPlayer1) {
		this.readyPlayer1 = readyPlayer1;
	}

	public boolean isReadyPlayer2() {
		return readyPlayer2;
	}

	public void setReadyPlayer2(boolean readyPlayer2) {
		this.readyPlayer2 = readyPlayer2;
	}

	public boolean isConvergencePlayer1() {
		return convergencePlayer1;
	}

	public void setConvergencePlayer1(boolean convergencePlayer1) {
		this.convergencePlayer1 = convergencePlayer1;
	}

	public boolean isConvergencePlayer2() {
		return convergencePlayer2;
	}

	public void setConvergencePlayer2(boolean convergencePlayer2) {
		this.convergencePlayer2 = convergencePlayer2;
	}



}