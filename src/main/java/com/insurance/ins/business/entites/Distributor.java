package com.insurance.ins.business.entites;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "distributors")
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(length = 60)
    private String passwordHash;

    @Column(length = 100)
    private String fullName;

    @OneToMany(mappedBy = "distributor")
    private Set<Contract> contracts = new HashSet<Contract>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Distributor(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public Distributor(Long id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Distributor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
