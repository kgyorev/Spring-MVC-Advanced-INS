package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;

import java.util.List;

public interface ContractService {
    List<Contract> findAll();
    Contract findById(Long id);
    Contract create(Contract contract);
    Contract edit(Contract contract);
    void deleteById(Long id);
    void cancel(Contract contract);
    void inForce(Contract contract);
}
