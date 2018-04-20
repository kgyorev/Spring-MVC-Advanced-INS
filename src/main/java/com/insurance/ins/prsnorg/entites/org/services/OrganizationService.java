package com.insurance.ins.prsnorg.entites.org.services;

import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.models.AllOrganizationsViewModel;
import com.insurance.ins.prsnorg.entites.org.models.EditOrganizationModel;
import com.insurance.ins.prsnorg.entites.org.models.SearchOrganizationModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationService extends FieldValueExists {
    List<Organization> findAll();

    AllOrganizationsViewModel findAllByPage(Pageable pageable);

    AllOrganizationsViewModel findAllByVatAndFullName(String vat, String fullName, Pageable pageable);

    AllOrganizationsViewModel findAllByFullName(String fullName, Pageable pageable);

    AllOrganizationsViewModel findAllByVat(String vat, Pageable pageable);

    default long getTotalPages() {
        return getTotalPages(10);
    }

    long getTotalPages(int size);

    Organization findById(Long id);

    Organization create(Organization client);

    Organization edit(Organization client, EditOrganizationModel organizationEdit);

    void deleteById(Long id);

    AllOrganizationsViewModel searchOrganization(SearchOrganizationModel searchOrganizationModel, Pageable pageable);
}
