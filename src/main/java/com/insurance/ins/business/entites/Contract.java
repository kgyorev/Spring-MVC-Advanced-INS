package com.insurance.ins.business.entites;

import com.insurance.ins.business.enums.Frequency;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contracts")
@Audited
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDt;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDt ;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDt;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextBillingDueDate;

    @Column(nullable = false)
    private Frequency frequency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double premiumAmount;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private Status status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Distributor distributor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Person owner;

    @OneToMany(mappedBy = "contract")
    private Set<Premium> premiums;
    @OneToMany(mappedBy = "contract")
    private Set<MoneyIn> moneyIns;

    public Contract() {
        this.frequency=Frequency.MONTHLY;
        this.creationDt = LocalDate.MIN;
        this.startDt = LocalDate.MIN;
        this.endDt = LocalDate.ofYearDay(9999,1);
        this.premiums = new HashSet<>();
        this.moneyIns = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEndDt() {
        return endDt;
    }

    public void setEndDt(LocalDate endDt) {
        this.endDt = endDt;
    }

    public Double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(Double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getStartDt() {
        return startDt;
    }

    public void setStartDt(LocalDate startDt) {
        this.startDt = startDt;
    }

    public LocalDate getCreationDt() {
        return creationDt;
    }

    public void setCreationDt(LocalDate creationDt) {
        this.creationDt = creationDt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getNextBillingDueDate() {
        return nextBillingDueDate;
    }

    public void setNextBillingDueDate(LocalDate nextBillingDueDate) {
        this.nextBillingDueDate = nextBillingDueDate;
    }

    public Set<Premium> getPremiums() {
        return premiums;
    }

    public void setPremiums(Set<Premium> premiums) {
        this.premiums = premiums;
    }

    public Set<MoneyIn> getMoneyIns() {
        return moneyIns;
    }

    public void setMoneyIns(Set<MoneyIn> moneyIns) {
        this.moneyIns = moneyIns;
    }
}
