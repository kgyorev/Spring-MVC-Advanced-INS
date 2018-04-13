package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.distributor.AllDistributorsViewModel;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.models.distributor.SearchDistributorModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DistributorService {
    List<Distributor> findAll();
    Distributor findById(Long id);
    Distributor create(Distributor distributor);
    Distributor edit(DistributorModel distributor);
    void deleteById(Long id);

    AllContractsViewModel searchContractsForDistributor(Distributor distributor, Pageable page);

    AllDistributorsViewModel searchDistributor(SearchDistributorModel searchDistributorModel, Pageable pageable);
}
