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



}
