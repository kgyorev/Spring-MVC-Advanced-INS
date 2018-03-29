package com.insurance.ins.prsnorg.entites.prsn.models;


import com.insurance.ins.prsnorg.entites.prsn.enums.Gender;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.utils.annotations.Unique;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class PersonModel {
    @Size(min=5,message="Please enter Full Name minimum 5 symbols")
    private String fullName;
    @Size(min=10,max=10, message="Please enter EGN with 10 numbers")
    @Unique(service = PersonService.class, fieldName = "egn", message = "There is person with this EGN, EGN must be unique")
    private String egn;
    @NotNull(message="Please enter Birth Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message="Please select Gender")
    private Gender sex;
    private Boolean smoker;

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

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public Boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) {
        this.smoker = smoker;
    }
}
