package com.insurance.ins.financial.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.models.AllPremiumsViewModel;
import com.insurance.ins.financial.models.PremiumModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PremiumService  {
    List<Premium> findAll();
    Premium findById(Long id);
    Premium create(Contract contract,Premium premium);
    void deleteById(Long id);
    void cancel(Premium premium);
    void tryToPay(Premium premium, MoneyIn moneyIn);
//    AllContractsViewModel findAllById(Long id, Pageable pageable);
//
//    AllContractsViewModel findAllByStatus(Status status, Pageable pageable);
//
//    AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable);
//    AllContractsViewModel findAllByPage(Pageable pageable);
//
     AllPremiumsViewModel searchPremiumForContract(Contract contract, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(10);
    }
    long getTotalPages(int size);

    PremiumModel createForView(Contract contract);

    Premium findOldestPendingPremium(Contract contract);
}
