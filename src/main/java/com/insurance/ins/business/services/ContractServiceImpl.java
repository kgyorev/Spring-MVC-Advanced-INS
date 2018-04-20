package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.enums.Frequency;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.contract.ContractModel;
import com.insurance.ins.business.models.contract.EditContractModel;
import com.insurance.ins.business.models.contract.SearchContractModel;
import com.insurance.ins.business.repositories.ContractRepository;
import com.insurance.ins.business.repositories.DistributorRepository;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.reposiotries.PersonRepository;
import com.insurance.ins.utils.DTOConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Primary
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final DistributorRepository distributorRepository;
    private final PersonRepository personRepository;
    private final ProductService productService;
    private final DistributorService distributorService;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, PersonRepository personRepository, ProductService productService, DistributorRepository distributorRepository, DistributorService distributorService) {
        this.contractRepository = contractRepository;
        this.personRepository = personRepository;
        this.productService = productService;
        this.distributorRepository = distributorRepository;
        this.distributorService = distributorService;
    }

    @Override
    public LocalDate calculateNextBillingDueDate(Contract contract) {

        Frequency frequency = contract.getFrequency();

       Long months =  this.getFrequencyMonths(frequency);

       return contract.getNextBillingDueDate().plusMonths(months);

    }

    private Long getFrequencyMonths(Frequency frequency) {
       switch (frequency){
           case ANUAL: return 12L;
           case SEMI_ANNUAL: return 6L;
           case TRIMESTER: return 3L;
           default: return 1L;
       }
    }

    @Override
    public List<Contract> findAll() {
        return this.contractRepository.findAll();
    }

    @Override
    public List<Contract> findAllByOwnerEgn(String ownerEgn) {
        return this.contractRepository.findAllByOwnerEgn(ownerEgn);
    }

    @Override
    public List<Contract> findAllByStatusAndOwnerEgn(Status status, String ownerEgn) {
        return this.contractRepository.findAllByStatusAndOwnerEgn(status,ownerEgn);
    }


    @Override
    public Contract findById(Long id) {
        return this.contractRepository.findById(id).orElse(null);
    }

    @Override
    public Contract create(Contract contract) {
        return this.contractRepository.saveAndFlush(contract);
    }

    @Override
    public void edit(Contract contract,EditContractModel contractModel) {
//        Contract editedContract = DTOConvertUtil.convert(contractModel, Contract.class);
        String distributorStr = contractModel.getDistributor();
        Distributor distributor = distributorRepository.getOne(Long.valueOf(distributorStr));
//        Contract contract = this.findById(contractModel.getId());
        contract.setFrequency(contractModel.getFrequency());
        contract.setDuration(contractModel.getDuration());
        contract.setAmount(contractModel.getAmount());
        contract.setDistributor(distributor);
        contract.setPremiumAmount(contractModel.getPremiumAmount());
        contract.setNextBillingDueDate(contractModel.getNextBillingDueDate());

//        editedContract.setOwner(contract.getOwner());
//        editedContract.setProduct(contract.getProduct());
//        editedContract.setCreationDt(contract.getCreationDt());

        this.contractRepository.saveAndFlush(contract);
    }

    @Override
    public Contract prepareContractForCreation(@Valid ContractModel contractModel) {
        String id_str = contractModel.getOwner();
        long id = Long.parseLong(id_str);
        Person person = this.personRepository.findById(id).orElse(null);
        String productIdntfr = contractModel.getProduct();
        Product product = this.productService.findByIdntfr(productIdntfr);
        Contract contract = DTOConvertUtil.convert(contractModel, Contract.class);
        contract.setOwner(person);
        contract.setProduct(product);
        String DistributorIdStr = contractModel.getDistributor();
        long DistributorId = Long.parseLong(DistributorIdStr);
        Distributor distributor = this.distributorService.findById(DistributorId);
        contract.setDistributor(distributor);
        LocalDate endDate = contractModel.getStartDt().plusYears(contract.getDuration());
        contract.setEndDt(endDate);
        contract.setCreationDt(LocalDate.now());
        contract.setStatus(Status.IN_FORCE);
        return contract;
    }

    @Override
    public Double calculatePremiumAmount(Contract contract) {
        int age = contract.getOwner().getAge(contract.getStartDt()) + 1;
        Double premiumAmount = age * contract.getAmount() / (contract.getDuration() * 12 * 100);
        return Math.round(premiumAmount * 100.0) / 100.0;
    }

    @Override
    public void deleteById(Long id) {
        this.contractRepository.deleteById(id);
    }

    @Override
    public void cancel(Contract contract) {
        contract.setStatus(Status.CANCELED);
        this.contractRepository.save(contract);
    }

    @Override
    public void inForce(Contract contract) {
        contract.setStatus(Status.IN_FORCE);
        this.contractRepository.save(contract);
    }

    @Override
    public AllContractsViewModel findAllById(Long id, Pageable pageable) {
        return null;
    }

    @Override
    public AllContractsViewModel findAllByStatus(Status status, Pageable pageable) {

        AllContractsViewModel viewModel = new AllContractsViewModel();

        viewModel.setContracts(this.contractRepository.findAllByStatus(status, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }
    @Override
    public AllContractsViewModel searchContractsForDistributor(Distributor distributor, Pageable page) {
        AllContractsViewModel viewModel = new AllContractsViewModel();
        viewModel.setContracts(this.contractRepository.findAllByDistributor(distributor, page));
        viewModel.setTotalPageCount(this.getTotalPages());
        return viewModel;
    }
    @Override
    public AllContractsViewModel findAllByIdAndStatus(Long id, Status status, Pageable pageable) {
        AllContractsViewModel viewModel = new AllContractsViewModel();

        viewModel.setContracts(this.contractRepository.findAllByIdAndStatus(id, status, pageable));
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
        if (!id.equals("") && !(status == null)) {
            allContractsViewModel = this.findAllByIdAndStatus(Long.parseLong(id), status, pageable);
        } else if (!id.equals("") && status == null) {
            allContractsViewModel = this.findAllById(Long.parseLong(id), pageable);
        } else if (id.equals("") && !(status == null)) {
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
