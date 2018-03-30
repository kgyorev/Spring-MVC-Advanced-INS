package com.insurance.ins.prsnorg.entites.org.models;


public class SearchOrganizationModel {
    private String vat;
    private String fullName;

    public SearchOrganizationModel() {
        this.vat="";
        this.fullName="";
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
