package com.insurance.ins.business.models.distributor;


public class SearchDistributorModel {
    private String referenceId;
    private String searchBy;

    public SearchDistributorModel() {
        this.referenceId = "";
        this.searchBy="distributorId";
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }
}
