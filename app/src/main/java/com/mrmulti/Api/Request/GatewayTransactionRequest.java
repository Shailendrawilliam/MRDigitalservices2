package com.mrmulti.Api.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GatewayTransactionRequest {

    @SerializedName("appid")
    @Expose
    private String appid;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("UMobile")
    @Expose
    private String uMobile;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("UPGID")
    @Expose
    private String uPGID;
    @SerializedName("OID")
    @Expose
    private int oID;
    @SerializedName("version")
    @Expose
    private String version;


    public GatewayTransactionRequest(String appid, String imei, String session, String userID, String uMobile, String amount, String uPGID, int oID, String version) {
        this.appid = appid;
        this.imei = imei;
        this.session = session;
        this.userID = userID;
        this.uMobile = uMobile;
        this.amount = amount;
        this.uPGID = uPGID;
        this.oID = oID;
        this.version = version;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUMobile() {
        return uMobile;
    }

    public void setUMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUPGID() {
        return uPGID;
    }

    public void setUPGID(String uPGID) {
        this.uPGID = uPGID;
    }

    public int getOID() {
        return oID;
    }

    public void setOID(int oID) {
        this.oID = oID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}


/*
public class GatewayTransactionRequest {

    @SerializedName("userID")
    @Expose
    public String userID;
    @SerializedName("sessionID")
    @Expose
    public String sessionID;
    @SerializedName("session")
    @Expose
    public String session;
    @SerializedName("appid")
    @Expose
    public String appid;
    @SerializedName("imei")
    @Expose
    public String imei;
    @SerializedName("regKey")
    @Expose
    public String regKey;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("serialNo")
    @Expose
    public String serialNo;
    @SerializedName("loginTypeID")
    @Expose
    public String loginTypeID;


    String amount;
    String upgid;
    String walletID;
    String oid;

    public GatewayTransactionRequest(String amount, String upgid, String walletID, String oid, String userID, String loginTypeID, String appid, String imei, String regKey, String version, String serialNo, String sessionID, String session) {
        this.amount = amount;
        this.upgid = upgid;
        this.walletID = walletID;
        this.oid = oid;
        this.userID = userID;
        this.sessionID = sessionID;
        this.session = session;
        this.appid = appid;
        this.imei = imei;
        this.regKey = regKey;
        this.version = version;
        this.serialNo = serialNo;
        this.loginTypeID = loginTypeID;
    }
}
*/
