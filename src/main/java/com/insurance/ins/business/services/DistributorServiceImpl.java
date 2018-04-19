package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.distributor.AllDistributorsViewModel;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.models.distributor.SearchDistributorModel;
import com.insurance.ins.business.repositories.ContractRepository;
import com.insurance.ins.business.repositories.DistributorRepository;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.reposiotries.OrganizationRepository;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.repositories.UserRepository;
import com.insurance.ins.utils.DTOConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DistributorServiceImpl implements DistributorService {

    private final DistributorRepository distributorRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public DistributorServiceImpl(DistributorRepository distributorRepository, ContractRepository contractRepository, UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.distributorRepository = distributorRepository;
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<Distributor> findAll() {
        return this.distributorRepository.findAll();
    }

    @Override
    public Distributor findById(Long id) {
        return this.distributorRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        this.distributorRepository.deleteById(id);
    }



    @Override
    public AllDistributorsViewModel searchDistributor(SearchDistributorModel searchDistributorModel, Pageable pageable) {
        String searchBy = searchDistributorModel.getSearchBy();
       String referenceIdStr =   searchDistributorModel.getReferenceId();
        if(referenceIdStr==null||referenceIdStr.equals("")){
            return  this.findAllByPage(pageable);
        }
        Long referenceId = Long.valueOf(searchDistributorModel.getReferenceId());


        switch (searchBy){
            case "userId": return this.findAllByUser(this.userRepository.getOne(referenceId),pageable);
            case "organizationId": return this.findAllByOrganization(this.organizationRepository.getOne(referenceId),pageable);
            default: return this.findAllById(referenceId,pageable);
        }
    }


    @Override
    public Distributor create(Distributor distributor) {

        return this.distributorRepository.saveAndFlush(distributor);
    }

    @Override
    public Distributor edit(DistributorModel distributor) {
        Distributor distributorDb = DTOConvertUtil.convert(distributor, Distributor.class);
        User user = this.userRepository.findById(Long.valueOf(distributor.getUser())).orElse(null);
        Organization organization = this.organizationRepository.findById(Long.valueOf(distributor.getOrganization())).orElse(null);
        distributorDb.setUser(user);
        distributorDb.setOrganization(organization);
        return this.distributorRepository.saveAndFlush(distributorDb);
    }



    @Override
    public AllDistributorsViewModel findAllById(Long id, Pageable pageable) {
        AllDistributorsViewModel viewModel = new AllDistributorsViewModel();
        viewModel.setDistributors(this.distributorRepository.findAllById(id, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());
        return viewModel;
     }

    @Override
    public AllDistributorsViewModel findAllByOrganization(Organization organization, Pageable pageable) {

        AllDistributorsViewModel viewModel = new AllDistributorsViewModel();

        viewModel.setDistributors(this.distributorRepository.findAllByOrganization(organization, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }
    @Override
    public AllDistributorsViewModel findAllByUser(User user, Pageable pageable) {

        AllDistributorsViewModel viewModel = new AllDistributorsViewModel();

        viewModel.setDistributors(this.distributorRepository.findAllByUser(user, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }


    @Override
    public AllDistributorsViewModel findAllByPage(Pageable pageable) {
        AllDistributorsViewModel viewModel = new AllDistributorsViewModel();

        viewModel.setDistributors(this.distributorRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.distributorRepository.count() / size;
    }


}
