package com.insurance.ins.prsnorg.entites.org.models;


import com.insurance.ins.utils.annotations.PastDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class EditOrganizationModel {
    private Long id;
    @Size(min=5,message="Please enter Organization Full Name minimum 5 symbols")
    private String fullName;
    @Size(min=10,max=10, message="Please enter VAT with 10 numbers")
    private String vat;
    @PastDate(message = "Can't put future date")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
