package com.insurance.ins.technical.models;


public class SearchUserModel {
    private String criteria;
    private String searchBy;

    public SearchUserModel() {
        this.criteria = "";
        this.searchBy="userId";
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }
}
