package com.insurance.ins.business.models.contract;

import com.insurance.ins.business.entites.Contract;
import org.springframework.data.domain.Page;

public class AllContractsViewModel {
    private Page<Contract> contracts;

    private long totalPageCount;

    public Page<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Page<Contract> contracts) {
        this.contracts = contracts;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
