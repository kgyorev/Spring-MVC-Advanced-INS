package com.insurance.ins.financial;

import com.insurance.ins.business.entites.Contract;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Audited
@Table(name = "premiums")
public class Premium extends FinancialRoot{

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contract contract;

    @OneToOne
    private MoneyIn moneyIn;


    public Premium() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public MoneyIn getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(MoneyIn moneyIn) {
        this.moneyIn = moneyIn;
    }
}
