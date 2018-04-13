package com.insurance.ins.prsnorg.entites.org;

import com.insurance.ins.prsnorg.entites.PrsnOrg;

import javax.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization extends PrsnOrg{

    @Column(nullable = false,unique = true)
    private String vat;

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }
}
