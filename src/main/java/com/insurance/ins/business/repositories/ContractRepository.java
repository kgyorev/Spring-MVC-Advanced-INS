package com.insurance.ins.business.repositories;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends
        JpaRepository<Contract, Long> {

    Page<Contract> findAllById(Long id, Pageable pageable);

    Page<Contract> findAllByStatus(Status status, Pageable pageable);

    Page<Contract> findAllByIdAndStatus(Long id, Status status, Pageable pageable);
}
