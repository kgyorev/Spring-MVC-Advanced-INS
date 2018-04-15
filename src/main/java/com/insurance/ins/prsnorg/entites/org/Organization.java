package com.insurance.ins.prsnorg.entites.org;

import com.insurance.ins.prsnorg.entites.PrsnOrg;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Audited
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
