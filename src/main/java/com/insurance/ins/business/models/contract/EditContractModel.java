package com.insurance.ins.business.models.contract;


import com.insurance.ins.business.enums.Frequency;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.utils.annotations.Existing;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class EditContractModel {

    private Long id;
    @Existing(service = DistributorService.class, fieldName = "id", message = "Distributor with this id not exists.")
    @Size(min = 1,message="Please enter Distributor Id")
    private String distributor;

    @NotNull(message="Please enter Contract Start Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDt;

    @NotNull(message="Please enter Next Billing Due  Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextBillingDueDate;
    @NotNull
    @Min(value=500,message = "Minimum Amiunt is 500 EUR") @Max(value = 100000 ,message = "Maximum Amount is 100000 EUR")
    private double amount;
    @NotNull
    @Min(value=1,message = "Minimum Duration is 1 year") @Max(value = 10 ,message = "Maximum Duration is 10 years")
    private int duration;

//    @Size(min = 1,message="Please enter Product Id")
//    @Existing(service = ProductService.class, fieldName = "idntfr", message = "Product with this Idntfr not exists.")
//    private String product;

    private Frequency frequency;

    private double premiumAmount;

    private String selectedTab;

    public EditContractModel() {
        this.selectedTab ="summary";
        this.frequency=Frequency.MONTHLY;
    }

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

//    public String getOwner() {
//        return owner;
//    }
//
//    public void setOwner(String owner) {
//        this.owner = owner;
//    }
//
//    public String getProduct() {
//        return product;
//    }
//
//    public void setProduct(String product) {
//        this.product = product;
//    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNextBillingDueDate() {
        return nextBillingDueDate;
    }

    public void setNextBillingDueDate(LocalDate nextBillingDueDate) {
        this.nextBillingDueDate = nextBillingDueDate;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
}
