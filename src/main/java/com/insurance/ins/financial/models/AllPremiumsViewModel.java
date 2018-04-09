package com.insurance.ins.financial.models;

import com.insurance.ins.financial.Premium;
import org.springframework.data.domain.Page;

public class AllPremiumsViewModel {
    private Page<Premium> premiums;

    private long totalPageCount;

    public Page<Premium> getPremiums() {
        return premiums;
    }

    public void setPremiums(Page<Premium> premiums) {
        this.premiums = premiums;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
