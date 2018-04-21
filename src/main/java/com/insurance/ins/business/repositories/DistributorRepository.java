package com.insurance.ins.business.repositories;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.technical.entites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributorRepository extends
        JpaRepository<Distributor, Long> {


    Page<Distributor> findAllById(Long id, Pageable pageable);

    Page<Distributor> findAllByUser(User user, Pageable pageable);

    Page<Distributor> findAllByOrganization(Organization organization, Pageable pageable);
    List<Distributor> findAllByIdOrFullNameContains(Long id, String fullName);

}



