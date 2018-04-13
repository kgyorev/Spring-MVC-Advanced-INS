package com.insurance.ins.financial.repositories;


import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyInRepository extends
        JpaRepository<MoneyIn, Long> {


    Page<MoneyIn> findAllByContract(Contract contract, Pageable pageable);
    Page<MoneyIn> findAllById(Long id, Pageable pageable);

    Page<MoneyIn> findAllByStatus(Status status, Pageable pageable);

    Page<MoneyIn> findAllByIdAndStatus(Long id, Status status, Pageable pageable);
    MoneyIn findFirstByStatusAndContractOrderByRecordDate(Status status, Contract contract);
}
