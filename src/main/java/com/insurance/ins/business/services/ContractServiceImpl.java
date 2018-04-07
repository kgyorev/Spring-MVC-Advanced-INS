package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.AllContractsViewModel;
import com.insurance.ins.business.models.SearchContractModel;
import com.insurance.ins.business.repositories.ContractRepository;
import com.insurance.ins.prsnorg.entites.prsn.reposiotries.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Primary
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final PersonRepository personRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, PersonRepository personRepository) {
        this.contractRepository = contractRepository;
        this.personRepository = personRepository;
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
        return this.contractRepository.saveAndFlush(contract);
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

    @Override
    public AllContractsViewModel findAllById(Long id, Pageable pageable) {
        return null;
    }

    @Override
    public AllContractsViewModel findAllByStatus(Status status, Pageable pageable) {

        AllContractsViewModel viewModel = new AllContractsViewModel();

        viewModel.setContracts(this.contractRepository.findAllByStatus(status,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable) {
        AllContractsViewModel viewModel = new AllContractsViewModel();

        viewModel.setContracts(this.contractRepository.findAllByIdAndStatus(id,status,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllContractsViewModel findAllByPage(Pageable pageable) {
        AllContractsViewModel viewModel = new AllContractsViewModel();

        viewModel.setContracts(this.contractRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllContractsViewModel searchContract(SearchContractModel searchContractModel, Pageable pageable) {
        String id = searchContractModel.getCntrctId();
        Status status = searchContractModel.getStatus();

        AllContractsViewModel allContractsViewModel;

//        if (!id.equals("") && !status.equals("")) {
//            allContractsViewModel = this.findAllByIdAndStatus(Long.parseLong(id), status, pageable);
//        } else if (!id.equals("") && status.equals("")) {
//            allContractsViewModel = this.findAllById(Long.parseLong(id), pageable);
//        } else if (id.equals("")&& !status.equals("")) {
//            allContractsViewModel = this.findAllByStatus(status, pageable);
//        } else {
//            allContractsViewModel = this.findAllByPage(pageable);
//        }
        if (!id.equals("") && !(status==null)) {
            allContractsViewModel = this.findAllByIdAndStatus(Long.parseLong(id), status, pageable);
        } else if (!id.equals("") && status==null) {
            allContractsViewModel = this.findAllById(Long.parseLong(id), pageable);
        } else if (id.equals("")&& !(status==null)) {
            allContractsViewModel = this.findAllByStatus(status, pageable);
        } else {
            allContractsViewModel = this.findAllByPage(pageable);
        }
        return allContractsViewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.contractRepository.count() / size;
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {

        if (!fieldName.equals("owner")) {
            throw new UnsupportedOperationException("Field name not supported");
        }
        if (value.equals("")) {
            return true;
        }

        return this.personRepository.findById(Long.parseLong(value.toString())).isPresent();
    }
}
