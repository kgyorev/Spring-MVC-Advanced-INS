package com.insurance.ins.business.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ContractModel {

    @Size(min = 1,message="Please enter Owner Id")
    private String owner;
    @NotNull(message="Please enter Contract Start Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDt;
    @NotNull
    @Min(value=500,message = "Minimum Amiunt is 500 EUR") @Max(value = 100000 ,message = "Maximum Amiunt is 100000 EUR")
    private double amount;
    @NotNull
    @Min(value=1,message = "Minimum Duration is 1 year") @Max(value = 10 ,message = "Maximum Duration is 10 years")
    private int duration;

    @Size(min = 1,message="Please enter Product Id")
    private String product;


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getStartDt() {
        return startDt;
    }

    public void setStartDt(LocalDate startDt) {
        this.startDt = startDt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
