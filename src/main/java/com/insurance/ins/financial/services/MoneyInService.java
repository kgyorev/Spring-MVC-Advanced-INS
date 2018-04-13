package com.insurance.ins.financial.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.models.AllMoneyInsViewModel;
import com.insurance.ins.financial.models.MoneyInModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MoneyInService {
    List<MoneyIn> findAll();
    MoneyIn findById(Long id);
    MoneyIn create(Contract contract, MoneyIn moneyIn);
    void deleteById(Long id);
    void cancel(MoneyIn moneyIn);
//    AllContractsViewModel findAllById(Long id, Pageable pageable);
//
//    AllContractsViewModel findAllByStatus(Status status, Pageable pageable);
//
//    AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable);
//    AllContractsViewModel findAllByPage(Pageable pageable);
//
     AllMoneyInsViewModel searchMoneyInsForContract(Contract contract, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(10);
    }
    long getTotalPages(int size);

    MoneyInModel createForView(Contract contract);

    MoneyIn findOldestPendingMoneyIn(Contract contract);

}
