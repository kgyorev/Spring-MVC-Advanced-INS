package com.insurance.ins.prsnorg.entites.prsn.models;


public class SearchPersonModel {
    private String egn;
    private String fullName;

    public SearchPersonModel() {
        this.egn="";
        this.fullName="";
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
