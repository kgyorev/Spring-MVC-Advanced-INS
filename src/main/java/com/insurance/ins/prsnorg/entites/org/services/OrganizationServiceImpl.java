package com.insurance.ins.prsnorg.entites.org.services;

import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.models.AllOrganizationsViewModel;
import com.insurance.ins.prsnorg.entites.org.models.SearchOrganizationModel;
import com.insurance.ins.prsnorg.entites.org.reposiotries.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Primary
@Transactional
public class OrganizationServiceImpl implements OrganizationService {


    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<Organization> findAll() {
        return this.organizationRepository.findAll();
    }

    @Override
    public AllOrganizationsViewModel findAllByPage(Pageable pageable) {
        AllOrganizationsViewModel viewModel = new AllOrganizationsViewModel();

        viewModel.setOrganizations(this.organizationRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllOrganizationsViewModel findAllByVatAndFullName(String vat, String fullName, Pageable pageable) {
        AllOrganizationsViewModel viewModel = new AllOrganizationsViewModel();

        viewModel.setOrganizations(this.organizationRepository.findAllByVatAndFullNameContains(vat,fullName,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllOrganizationsViewModel findAllByFullName(String fullName, Pageable pageable) {
        AllOrganizationsViewModel viewModel = new AllOrganizationsViewModel();

        viewModel.setOrganizations(this.organizationRepository.findAllByFullNameContains(fullName,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllOrganizationsViewModel findAllByVat(String vat, Pageable pageable) {
        AllOrganizationsViewModel viewModel = new AllOrganizationsViewModel();

        viewModel.setOrganizations(this.organizationRepository.findAllByVat(vat,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.organizationRepository.count() / size;
    }
    @Override
    public Organization findById(Long id) {
        return this.organizationRepository.findById(id).orElse(null);
    }

    @Override
    public Organization create(Organization organization) {
        return this.organizationRepository.saveAndFlush(organization);
    }

    @Override
    public Organization edit(Organization organization) {
        return this.organizationRepository.save(organization);
    }

    @Override
    public void deleteById(Long id) {
        this.organizationRepository.deleteById(id);
    }

    @Override
    public AllOrganizationsViewModel searchOrganization(SearchOrganizationModel searchOrganizationModel, Pageable pageable) {

        String vat = searchOrganizationModel.getVat();
        String fullName = searchOrganizationModel.getFullName();

        AllOrganizationsViewModel organizationAll;

        if (!vat.equals("") && !fullName.equals("")) {
            organizationAll = this.findAllByVatAndFullName(vat, fullName, pageable);
        } else if (!vat.equals("") && fullName.equals("")) {
            organizationAll = this.findAllByVat(vat, pageable);
        } else if (vat.equals("")&& !fullName.equals("")) {
            organizationAll = this.findAllByFullName(fullName, pageable);
        } else {
            organizationAll = this.findAllByPage(pageable);
        }
        return organizationAll;
    }
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        Assert.notNull(fieldName,"Can't be null");

        if (!fieldName.equals("vat")) {
            throw new UnsupportedOperationException("Field name not supported");
        }

        if (value == null) {
            return false;
        }

        return this.organizationRepository.existsByVat(value.toString());
    }
}
