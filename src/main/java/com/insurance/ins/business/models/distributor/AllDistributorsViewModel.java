package com.insurance.ins.business.models.distributor;

import com.insurance.ins.business.entites.Distributor;
import org.springframework.data.domain.Page;

public class AllDistributorsViewModel {
    private Page<Distributor> distributors;

    private long totalPageCount;

    public Page<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(Page<Distributor> distributors) {
        this.distributors = distributors;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
