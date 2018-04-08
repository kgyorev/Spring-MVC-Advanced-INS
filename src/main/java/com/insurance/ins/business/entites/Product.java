package com.insurance.ins.business.entites;

import javax.persistence.*;

/**
 * Created by K on 30.3.2018 г..
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String idntfr;
    @Column(nullable = false, length = 30, unique = true)
    private String label;

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

    @Override
    public String toString() {
        return this.idntfr;
    }
}
