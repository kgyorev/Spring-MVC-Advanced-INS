package com.insurance.ins.technical.models;

import java.util.Date;

public class LogDetailsModel {


    private String name;
    private Date time;
    private String operation;
    private String modifiedTable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getModifiedTable() {
        return modifiedTable;
    }

    public void setModifiedTable(String modifiedTable) {
        this.modifiedTable = modifiedTable;
    }
}
