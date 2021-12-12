package com.mrmulti.DMR.dto;

/**
 * Created by Lalit on 12-04-2017.
 */

public class DMR {
    private TABLE TABLE;
    private String MESSAGE;

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public com.mrmulti.DMR.dto.TABLE getTABLE() {
        return TABLE;
    }

    public void setTABLE(com.mrmulti.DMR.dto.TABLE TABLE) {
        this.TABLE = TABLE;
    }
}
