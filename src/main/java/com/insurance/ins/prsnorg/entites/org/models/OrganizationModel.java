package com.insurance.ins.prsnorg.entites.org.models;


import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.utils.annotations.Unique;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class OrganizationModel {
    @Size(min=5,message="Please enter Organization Full Name minimum 5 symbols")
    private String fullName;
    @Size(min=10,max=10, message="Please enter VAT with 10 numbers")
    @Unique(service = OrganizationService.class, fieldName = "vat", message = "There is organization with this VAT, VAT must be unique")
    private String vat;
    @NotNull(message="Please Organization Creation date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }
}
