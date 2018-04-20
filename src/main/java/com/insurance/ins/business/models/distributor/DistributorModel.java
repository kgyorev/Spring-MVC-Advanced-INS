package com.insurance.ins.business.models.distributor;


import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.technical.services.UserServiceImpl;
import com.insurance.ins.utils.annotations.Existing;

import javax.validation.constraints.Size;

public class DistributorModel {
    private Long id;
    @Size(min=5,message="Please enter Full Name minimum 5 symbols")
    @Size(max=100,message="Please enter Full Name maximum 100 symbols")
    private String fullName;
    @Existing(service = UserServiceImpl.class, fieldName = "id", message = "User with this id not exists.")
    @Size(min = 1,message="Please enter User Id")
    private String user;
    @Size(min = 1,message="Please enter Reference Organization Id")
    @Existing(service = OrganizationService.class, fieldName = "id", message = "Organization with this id not exists.")
    private String organization;
    private String selectedTab;


    public DistributorModel() {
        this.selectedTab ="summary";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
}
