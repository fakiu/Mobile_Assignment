package com.example.chanys.assignment;

public class Court {
    private String SportName;
    private String CourtName;
    private String CourtAddress;
    private String CourtTelephone;

    public Court(String sportName, String courtName, String courtAddress, String courtTelephone) {
        SportName = sportName;
        CourtName = courtName;
        CourtAddress = courtAddress;
        CourtTelephone = courtTelephone;
    }

    public String getSportName() {
        return SportName;
    }

    public void setSportName(String sportName) {
        SportName = sportName;
    }

    public String getCourtName() {
        return CourtName;
    }

    public void setCourtName(String courtName) {
        CourtName = courtName;
    }

    public String getCourtAddress() {
        return CourtAddress;
    }

    public void setCourtAddress(String courtAddress) {
        CourtAddress = courtAddress;
    }

    public String getCourtTelephone() {
        return CourtTelephone;
    }

    public void setCourtTelephone(String courtTelephone) {
        CourtTelephone = courtTelephone;
    }
}
