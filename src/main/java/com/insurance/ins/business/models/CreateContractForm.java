package com.insurance.ins.business.models;



import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateContractForm {

    @Size(min = 1,message="Please enter Client Id")
    private String clientId;
    @Size(min = 2,message="Please enter Start Date")
    private String startdt;
    @NotNull
    @Min(value=500,message = "Minimum Amiunt is 500 EUR") @Max(value = 100000 ,message = "Maximum Amiunt is 100000 EUR")
    private double amount;
    @NotNull
    @Min(value=1,message = "Minimum Duration is 1 year") @Max(value = 10 ,message = "Maximum Duration is 10 years")
    private int duration;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStartdt() {
        return startdt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartdt(String startdt) {
        this.startdt = startdt;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
