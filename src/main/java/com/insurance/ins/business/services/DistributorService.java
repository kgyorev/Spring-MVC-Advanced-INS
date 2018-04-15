package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.distributor.AllDistributorsViewModel;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.models.distributor.SearchDistributorModel;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.technical.entites.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DistributorService {
    List<Distributor> findAll();
    Distributor findById(Long id);
    Distributor create(Distributor distributor);
    Distributor edit(DistributorModel distributor);
    void deleteById(Long id);

    AllDistributorsViewModel searchDistributor(SearchDistributorModel searchDistributorModel, Pageable pageable);

    default long getTotalPages() {
        return getTotalPages(10);
    }

    AllDistributorsViewModel findAllById(Long id, Pageable pageable);

    AllDistributorsViewModel findAllByOrganization(Organization organization, Pageable pageable);

    AllDistributorsViewModel findAllByUser(User user, Pageable pageable);

    AllDistributorsViewModel findAllByPage(Pageable pageable);

    long getTotalPages(int size);


}
