package com.insurance.ins.prsnorg.entites.prsn;

import com.insurance.ins.prsnorg.entites.PrsnOrg;
import com.insurance.ins.prsnorg.entites.prsn.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "persons")
public class Person extends PrsnOrg {

    @Column(nullable = false)
    private Gender sex;

    @Column(nullable = false)
    private Boolean smoker;

    @Column(nullable = false,unique = true)
    private String egn;
    public int getAge(LocalDate dateNow) {
//         LocalDate dateNowLocal = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate birthdtLocal = this.getStartDate();

        int diffInYears = (int) ChronoUnit.YEARS.between(birthdtLocal, dateNow);

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
