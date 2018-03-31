package com.insurance.ins.business.repositories;

import com.insurance.ins.business.entites.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends
        JpaRepository<Distributor, Long> {
}



