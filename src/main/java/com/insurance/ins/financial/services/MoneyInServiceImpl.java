package com.insurance.ins.financial.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.repositories.ContractRepository;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.enums.Status;
import com.insurance.ins.financial.models.AllMoneyInsViewModel;
import com.insurance.ins.financial.models.MoneyInModel;
import com.insurance.ins.financial.repositories.MoneyInRepository;
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
public class MoneyInServiceImpl implements MoneyInService {

    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final PremiumRepository premiumRepository;
    private final MoneyInRepository moneyInRepository;

    @Autowired
    public MoneyInServiceImpl(ContractRepository contractRepository, ContractService contractService, PremiumRepository premiumRepository, MoneyInRepository moneyInRepository) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
        this.premiumRepository = premiumRepository;
        this.moneyInRepository = moneyInRepository;
    }

    @Override
    public List<MoneyIn> findAll() {
        return this.moneyInRepository.findAll();
    }


    @Override
    public MoneyIn findById(Long id) {
        return this.moneyInRepository.findById(id).orElse(null);
    }

    @Override
    public MoneyIn create(Contract contract,MoneyIn moneyIn) {
        moneyIn.setRecordDate(LocalDate.now());
        LocalDate nextBillingDueDate = contractService.calculateNextBillingDueDate(contract);
        contract.setNextBillingDueDate(nextBillingDueDate);
        moneyIn.setContract(contract);
        this.contractRepository.saveAndFlush(contract);
        return this.moneyInRepository.saveAndFlush(moneyIn);
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
        this.moneyInRepository.deleteById(id);
    }

    @Override
    public void cancel(MoneyIn moneyIn) {
        moneyIn.setStatus(Status.CANCELED);
        this.moneyInRepository.save(moneyIn);
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
    public AllMoneyInsViewModel searchMoneyInsForContract(Contract contract, Pageable pageable) {


        AllMoneyInsViewModel allMoneyInsViewModel = new AllMoneyInsViewModel();

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

        Page<MoneyIn> allMoneyInsByContract = moneyInRepository.findAllByContract(contract, pageable);
        allMoneyInsViewModel.setMoneyIns(allMoneyInsByContract);
        allMoneyInsViewModel.setTotalPageCount(this.getTotalPages());
        return allMoneyInsViewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.contractRepository.count() / size;
    }

    @Override
    public MoneyInModel createForView(Contract contract) {

        MoneyInModel moneyInModel = new MoneyInModel();
        moneyInModel.setOperationAmount(contract.getPremiumAmount());
        moneyInModel.setCntrctId(String.valueOf(contract.getId()));
        return moneyInModel;
    }

    @Override
    public MoneyIn findOldestPendingMoneyIn(Contract contract) {
        MoneyIn allByStatusOrderByRecordDate = this.moneyInRepository.findFirstByStatusAndContractOrderByRecordDate(Status.PENDING,contract);
        return allByStatusOrderByRecordDate;
    }
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

