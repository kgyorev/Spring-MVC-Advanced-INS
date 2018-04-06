package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.AllContractsViewModel;
import com.insurance.ins.business.models.SearchContractModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractService  extends FieldValueExists {
    List<Contract> findAll();
    Contract findById(Long id);
    Contract create(Contract contract);
    Contract edit(Contract contract);
    void deleteById(Long id);
    void cancel(Contract contract);
    void inForce(Contract contract);
    AllContractsViewModel findAllById(Long id, Pageable pageable);

    AllContractsViewModel findAllByStatus(Status status, Pageable pageable);

    AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable);
    AllContractsViewModel findAllByPage(Pageable pageable);

    AllContractsViewModel searchContract(SearchContractModel searchContractModel, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(10);
    }
    long getTotalPages(int size);

}
