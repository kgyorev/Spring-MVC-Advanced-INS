package com.insurance.ins.business.entites;


import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.technical.entites.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "distributors")
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String fullName;

    @OneToOne
    private User user;

    @OneToOne
    private Organization organization;

    @OneToMany(mappedBy = "distributor")
    private Set<Contract> contracts = new HashSet<Contract>();

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

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public Distributor() {
    }

    public Distributor(String fullName) {
        this.fullName = fullName;
    }

    public Distributor(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
