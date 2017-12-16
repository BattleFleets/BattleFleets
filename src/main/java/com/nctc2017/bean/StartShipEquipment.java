package com.nctc2017.bean;

public class StartShipEquipment {

    protected int startCannonType;
    protected int startMastType;
    protected int startNumCannon;
    protected int startNumMast;

    public static final String START_CANNON_TYPE = "StartCannonType";
    public static final String START_MAST_TYPE = "StartMastType";
    public static final String START_NUM_CANNON = "StartNumCannon";
    public static final String START_NUM_MAST = "StartNumMast";

    public StartShipEquipment(int startCannonType, int startMastType, int startNumCannon, int startNumMast) {
        this.startMastType = startMastType;
        this.startCannonType = startCannonType;
        this.startNumCannon = startNumCannon;
        this.startNumMast =startNumMast;
    }

}
