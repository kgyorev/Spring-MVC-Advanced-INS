package com.insurance.ins.prsnorg.entites.prsn.entities;

import com.insurance.ins.prsnorg.entites.PrsnOrg;
import com.insurance.ins.prsnorg.entites.prsn.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "persons")
public class Person extends PrsnOrg {

    @Column(nullable = false)
    private Gender sex;

    @Column(nullable = false)
    private Boolean smoker;

    @Column(nullable = false,unique = true)
    private String egn;
    public int getAge(Date dateNow) {
         LocalDate dateNowLocal = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate birthdtLocal = this.getStartDate();

        int diffInYears = (int) ChronoUnit.YEARS.between(birthdtLocal, dateNowLocal);

        return diffInYears;
    }

    public Boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) {
        this.smoker = smoker;
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


}
