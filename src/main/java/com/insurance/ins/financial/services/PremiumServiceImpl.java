package com.insurance.ins.financial.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.repositories.ContractRepository;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.enums.Status;
import com.insurance.ins.financial.models.AllPremiumsViewModel;
import com.insurance.ins.financial.models.PremiumModel;
import com.insurance.ins.financial.repositories.PremiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Primary
public class PremiumServiceImpl implements PremiumService {

    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final PremiumRepository premiumRepository;

    @Autowired
    public PremiumServiceImpl(ContractRepository contractRepository, ContractService contractService, PremiumRepository premiumRepository) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
        this.premiumRepository = premiumRepository;
    }

    @Override
    public List<Premium> findAll() {
        return this.premiumRepository.findAll();
    }


    @Override
    public Premium findById(Long id) {
        return this.premiumRepository.getOne(id);
    }

    @Override
    public Premium create(Contract contract,Premium premium) {
        premium.setRecordDate(LocalDate.now());
        LocalDate nextBillingDueDate = contractService.calculateNextBillingDueDate(contract);
        contract.setNextBillingDueDate(nextBillingDueDate);
        premium.setContract(contract);
        this.contractRepository.saveAndFlush(contract);
        return this.premiumRepository.saveAndFlush(premium);
    }

//    @Override
//    public Contract edit(ContractModel contractModel) {
//
//        Contract contract = this.findById(contractModel.getId());
//        contract.setFrequency(contractModel.getFrequency());
//        contract.setDuration(contractModel.getDuration());
//        contract.setAmount(contractModel.getAmount());
//        contract.setPremiumAmount(contractModel.getPremiumAmount());
//        contract.setNextBillingDueDate(contractModel.getNextBillingDueDate());
//        return this.contractRepository.saveAndFlush(contract);
//    }

    @Override
    public void deleteById(Long id) {
        this.premiumRepository.deleteById(id);
    }

    @Override
    public void cancel(Premium premium) {
        premium.setStatus(Status.CANCELED);
        this.premiumRepository.save(premium);
    }

    @Override
    public void pay(Premium premium) {
        premium.setStatus(Status.PAID);
        this.premiumRepository.save(premium);
    }

//    @Override
//    public AllContractsViewModel findAllById(Long id, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public AllContractsViewModel findAllByStatus(Status status, Pageable pageable) {
//
//        AllContractsViewModel viewModel = new AllContractsViewModel();
//
//        viewModel.setContracts(this.contractRepository.findAllByStatus(status, pageable));
//        viewModel.setTotalPageCount(this.getTotalPages());
//
//        return viewModel;
//    }
//
//    @Override
//    public AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable) {
//        AllContractsViewModel viewModel = new AllContractsViewModel();
//
//        viewModel.setContracts(this.contractRepository.findAllByIdAndStatus(id, status, pageable));
//        viewModel.setTotalPageCount(this.getTotalPages());
//
//        return viewModel;
//    }
//
//    @Override
//    public AllContractsViewModel findAllByPage(Pageable pageable) {
//        AllContractsViewModel viewModel = new AllContractsViewModel();
//
//        viewModel.setContracts(this.contractRepository.findAll(pageable));
//        viewModel.setTotalPageCount(this.getTotalPages());
//
//        return viewModel;
//    }

    @Override
    public AllPremiumsViewModel searchPremiumForContract(Contract contract, Pageable pageable) {


        AllPremiumsViewModel allPremiumsViewModel = new AllPremiumsViewModel();

//        if (!id.equals("") && !status.equals("")) {
//            allContractsViewModel = this.findAllByIdAndStatus(Long.parseLong(id), status, pageable);
//        } else if (!id.equals("") && status.equals("")) {
//            allContractsViewModel = this.findAllById(Long.parseLong(id), pageable);
//        } else if (id.equals("")&& !status.equals("")) {
//            allContractsViewModel = this.findAllByStatus(status, pageable);
//        } else {
//            allContractsViewModel = this.findAllByPage(pageable);
//        }
//        if (!id.equals("") && !(status == null)) {
//            allContractsViewModel = this.findAllByIdAndStatus(Long.parseLong(id), status, pageable);
//        } else if (!id.equals("") && status == null) {
//            allContractsViewModel = this.findAllById(Long.parseLong(id), pageable);
//        } else if (id.equals("") && !(status == null)) {
//            allContractsViewModel = this.findAllByStatus(status, pageable);
//        } else {
//            allContractsViewModel = this.findAllByPage(pageable);
//        }

        Page<Premium> allPremiumsByContract = premiumRepository.findAllByContract(contract, pageable);
        allPremiumsViewModel.setPremiums(allPremiumsByContract);
        allPremiumsViewModel.setTotalPageCount(this.getTotalPages());
        return allPremiumsViewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.contractRepository.count() / size;
    }

    @Override
    public PremiumModel createForView(Contract contract) {

        PremiumModel premiumModel = new PremiumModel();
        premiumModel.setOperationAmount(contract.getPremiumAmount());


        LocalDate nextBillingDueDate = contract.getNextBillingDueDate();

        LocalDate nextBillingDueDateNew  =  contractService.calculateNextBillingDueDate(contract);

        premiumModel.setStartDate(nextBillingDueDate);
        premiumModel.setEndDate(nextBillingDueDateNew.minusDays(1));
        premiumModel.setOperationAmount(contract.getPremiumAmount());
        premiumModel.setCntrctId(String.valueOf(contract.getId()));
        return premiumModel;
    }

//    @Override
//    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
//
//        if (!fieldName.equals("owner")) {
//            throw new UnsupportedOperationException("Field name not supported");
//        }
//        if (value.equals("")) {
//            return true;
//        }
//
//        return this.personRepository.findById(Long.parseLong(value.toString())).isPresent();
//    }
}