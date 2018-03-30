package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Primary
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public List<Contract> findAll() {
        return this.contractRepository.findAll();
    }


    @Override
    public Contract findById(Long id) {
        return this.contractRepository.getOne(id);
    }

    @Override
    public Contract create(Contract contract) {
        return this.contractRepository.save(contract);
    }

    @Override
    public Contract edit(Contract contract) {
        return this.contractRepository.save(contract);
    }

    @Override
    public void deleteById(Long id) {
        this.contractRepository.deleteById(id);
    }
    @Override
    public void cancel(Contract contract) {
        contract.setStatus(Status.CANCELED);
        this.contractRepository.save(contract);}
    @Override
    public void inForce(Contract contract) {
        contract.setStatus(Status.IN_FORCE);
        this.contractRepository.save(contract);}
}
