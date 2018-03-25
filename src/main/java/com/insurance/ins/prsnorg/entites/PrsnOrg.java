package com.insurance.ins.prsnorg.entites;


import com.insurance.ins.business.entites.Contract;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class PrsnOrg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String fullName;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate = LocalDate.MIN;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate = LocalDate.ofYearDay(9999,1);


    @OneToMany(mappedBy = "owner")
    private Set<Contract> contracts = new HashSet<Contract>();

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public PrsnOrg() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
