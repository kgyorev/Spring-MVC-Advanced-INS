package com.insurance.ins.financial.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by K on 9.4.2018 г..
 */
public class MoneyInModel {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;
    private Double operationAmount;
    private String cntrctId;

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Double getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(Double operationAmount) {
        this.operationAmount = operationAmount;
    }

    public String getCntrctId() {
        return cntrctId;
    }

    public void setCntrctId(String cntrctId) {
        this.cntrctId = cntrctId;
    }
}
