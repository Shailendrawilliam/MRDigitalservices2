package com.mrmulti.popupdataresponse;

import java.util.ArrayList;

public class PopupdataResponse {


    private String RESPONSESTATUS;
    private ArrayList<DataObject> DATA;

    public ArrayList<com.mrmulti.popupdataresponse.DataObject> getDATA() {
        return DATA;
    }

    public void setDATA(ArrayList<com.mrmulti.popupdataresponse.DataObject> DATA) {
        this.DATA = DATA;
    }



    public String getRESPONSESTATUS() {
        return RESPONSESTATUS;
    }

    public void setRESPONSESTATUS(String RESPONSESTATUS) {
        this.RESPONSESTATUS = RESPONSESTATUS;
    }


}
