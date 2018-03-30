package com.insurance.ins.business.repositories;

import com.insurance.ins.business.entites.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends
        JpaRepository<Contract, Long> {
}
