package com.insurance.ins.business.entites;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date startdt = new Date();

    @Column(nullable = false)
    private Date enddt = new Date();

    @Column(nullable = false)
    private Date creationdt = new Date();

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double premiumamount;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String status;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Distributor distributor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Person owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEnddt() {
        return enddt;
    }

    public void setEnddt(Date enddt) {
        this.enddt = enddt;
    }

    public Double getPremiumamount() {
        return premiumamount;
    }

    public void setPremiumamount(Double premiumamount) {
        this.premiumamount = premiumamount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartdt() {
        return startdt;
    }

    public void setStartdt(Date startdt) {
        this.startdt = startdt;
    }

    public Date getCreationdt() {
        return creationdt;
    }

    public void setCreationdt(Date creationdt) {
        this.creationdt = creationdt;
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
}
