package com.insurance.ins.business.repositories;

import com.insurance.ins.business.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends
        JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Product findByIdntfr(String idntfr);
    Page<Product> findAllByIdntfr(String idntfr, Pageable pageable);
    List<Product> findAllByIdntfr(String idntfr);
    List<Product> findAllByIdntfrContains(String idntfr);

    boolean existsByIdntfr(String s);
}
