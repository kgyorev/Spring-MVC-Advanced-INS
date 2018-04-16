package com.insurance.ins.business.entites;

import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by K on 30.3.2018 г..
 */
@Entity
@Audited
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String idntfr;
    @Column(nullable = false, length = 30)
    private String label;

    @Column(nullable = false)
    private int minAge;

    @Column(nullable = false)
    private int maxAge;

    @Column(nullable = false)
    private String frequencyRule;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdntfr() {
        return idntfr;
    }

    public void setIdntfr(String idntfr) {
        this.idntfr = idntfr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getFrequencyRule() {
        return frequencyRule;
    }

    public void setFrequencyRule(String frequencyRule) {
        this.frequencyRule = frequencyRule;
    }

    @Override
    public String toString() {
        return this.idntfr;
    }
}
