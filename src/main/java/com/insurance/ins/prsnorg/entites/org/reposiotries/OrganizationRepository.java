package com.insurance.ins.prsnorg.entites.org.reposiotries;


import com.insurance.ins.prsnorg.entites.org.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends  JpaRepository<Organization, Long> {

    Boolean existsByVat(String vat);
    Page<Organization> findAllByVatAndFullNameContains(String vat, String fullName, Pageable pageable);
    Page<Organization> findAllByFullName(String fullName, Pageable pageable);
    Page<Organization> findAllByFullNameContains(String fullName, Pageable pageable);
    Page<Organization> findAllByVat(String vat, Pageable pageable);
}



