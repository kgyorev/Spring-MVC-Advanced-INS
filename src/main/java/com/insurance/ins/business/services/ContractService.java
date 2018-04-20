package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.contract.ContractModel;
import com.insurance.ins.business.models.contract.EditContractModel;
import com.insurance.ins.business.models.contract.SearchContractModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public interface ContractService  extends FieldValueExists {
    LocalDate calculateNextBillingDueDate(Contract contract);
    List<Contract> findAll();
    List<Contract> findAllByOwnerEgn(String ownerEgn);
    List<Contract> findAllByStatusAndOwnerEgn(Status status, String ownerEgn);
    Contract findById(Long id);
    Contract create(Contract contract);

    Contract prepareContractForEdit(Contract contract, @Valid EditContractModel contractModel);

    void deleteById(Long id);
    void cancel(Contract contract);
    void inForce(Contract contract);
    AllContractsViewModel findAllById(Long id, Pageable pageable);

    AllContractsViewModel findAllByStatus(Status status, Pageable pageable);

    AllContractsViewModel searchContractsForDistributor(Distributor distributor, Pageable page);

    AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable);
    AllContractsViewModel findAllByPage(Pageable pageable);

    AllContractsViewModel searchContract(SearchContractModel searchContractModel, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(10);
    }
    long getTotalPages(int size);


    void edit(Contract contract, @Valid EditContractModel contractModel);

    Contract prepareContractForCreation(@Valid ContractModel contractModel);

    Double calculatePremiumAmount(Contract contract);

}
