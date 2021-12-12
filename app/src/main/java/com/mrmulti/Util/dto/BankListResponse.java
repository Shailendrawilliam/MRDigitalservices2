package com.mrmulti.Util.dto;

import java.util.ArrayList;

/**
 * Created by Lalit on 12-04-2017.
 */

public class BankListResponse {

    private String RESPONSESTATUS;
    private String message;
    private ArrayList<BankListObject> Banks;

    public String getRESPONSESTATUS() {
        return RESPONSESTATUS;
    }

    public void setRESPONSESTATUS(String RESPONSESTATUS) {
        this.RESPONSESTATUS = RESPONSESTATUS;
    }

    public ArrayList<BankListObject> getBanks() {
        return Banks;
    }

    public void setBanks(ArrayList<BankListObject> banks) {
        Banks = banks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
