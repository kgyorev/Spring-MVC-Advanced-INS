package com.insurance.ins.prsnorg.entites.org.models;

import com.insurance.ins.prsnorg.entites.org.Organization;
import org.springframework.data.domain.Page;

public class AllOrganizationsViewModel {
    private Page<Organization> organizations;

    private long totalPageCount;

    public Page<Organization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(Page<Organization> organizations) {
        this.organizations = organizations;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
