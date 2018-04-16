package com.insurance.ins.financial.repositories;


import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremiumRepository extends
        JpaRepository<Premium, Long> {


    List<Premium> findAllByContract_OwnerEgn(String contractOwnerEgn);
    List<Premium> findAllByStatusAndContract_OwnerEgn(Status status, String contractOwnerEgn);
    Page<Premium> findAllByContract(Contract contract, Pageable pageable);
    Page<Premium> findAllById(Long id, Pageable pageable);

    Page<Premium> findAllByStatus(Status status, Pageable pageable);

    Page<Premium> findAllByIdAndStatus(Long id, Status status, Pageable pageable);
    Premium findFirstByStatusAndContractOrderByRecordDate(Status status, Contract contract);
}
