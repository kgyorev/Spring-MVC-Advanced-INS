package com.insurance.ins.financial.repositories;


import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiumRepository extends
        JpaRepository<Premium, Long> {


    Page<Premium> findAllByContract(Contract contract, Pageable pageable);
    Page<Premium> findAllById(Long id, Pageable pageable);

    Page<Premium> findAllByStatus(Status status, Pageable pageable);

    Page<Premium> findAllByIdAndStatus(Long id, Status status, Pageable pageable);
}
