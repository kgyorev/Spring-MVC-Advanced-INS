package com.insurance.ins.financial;

import com.insurance.ins.business.entites.Contract;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
@Table(name = "money_ins")
public class MoneyIn extends FinancialRoot {


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contract contract;

    @OneToOne
    private Premium premium;


    public MoneyIn() {
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Premium getPremium() {
        return premium;
    }

    public void setPremium(Premium premium) {
        this.premium = premium;
    }
}
