package com.insurance.ins.business.models;


import com.insurance.ins.business.enums.Status;

public class SearchContractModel {
    private String cntrctId;

    public String getCntrctId() {
        return cntrctId;
    }
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCntrctId(String cntrctId) {
        this.cntrctId = cntrctId;
    }
}
