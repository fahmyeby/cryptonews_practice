package com.example.crypto_practice.model;

import java.util.Date;
import java.util.List;

public class News {
    private Integer ID;
    private String GUID;
    private Date PUBLISHED_ON;
    private String IMAGE_URL;
    private String TITLE;
    private String URL;
    private String BODY;
    private List<String> CATEGORY;
    public News() {
    }
    public News(Integer iD, String gUID, Date pUBLISHED_ON, String iMAGE_URL, String tITLE, String uRL, String bODY,
    List<String> cATEGORY) {
        ID = iD;
        GUID = gUID;
        PUBLISHED_ON = pUBLISHED_ON;
        IMAGE_URL = iMAGE_URL;
        TITLE = tITLE;
        URL = uRL;
        BODY = bODY;
        CATEGORY = cATEGORY;
    }
    public Integer getID() {
        return ID;
    }
    public void setID(Integer iD) {
        ID = iD;
    }
    public String getGUID() {
        return GUID;
    }
    public void setGUID(String gUID) {
        GUID = gUID;
    }
    public Date getPUBLISHED_ON() {
        return PUBLISHED_ON;
    }
    public void setPUBLISHED_ON(Date pUBLISHED_ON) {
        PUBLISHED_ON = pUBLISHED_ON;
    }
    public String getIMAGE_URL() {
        return IMAGE_URL;
    }
    public void setIMAGE_URL(String iMAGE_URL) {
        IMAGE_URL = iMAGE_URL;
    }
    public String getTITLE() {
        return TITLE;
    }
    public void setTITLE(String tITLE) {
        TITLE = tITLE;
    }
    public String getURL() {
        return URL;
    }
    public void setURL(String uRL) {
        URL = uRL;
    }
    public String getBODY() {
        return BODY;
    }
    public void setBODY(String bODY) {
        BODY = bODY;
    }
    public List<String> getCATEGORY() {
        return CATEGORY;
    }
    public void setCATEGORY(List<String> cATEGORY) {
        CATEGORY = cATEGORY;
    }
    @Override
    public String toString() {
        return ID + ", " + GUID + ", " + PUBLISHED_ON + ", " + IMAGE_URL
                + ", " + TITLE + ", " + URL + ", " + BODY + ", " + CATEGORY;
    }

    
}
