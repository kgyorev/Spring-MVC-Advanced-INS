package com.insurance.ins.financial.models;

import com.insurance.ins.financial.MoneyIn;
import org.springframework.data.domain.Page;

public class AllMoneyInsViewModel {
    private Page<MoneyIn> moneyIns;

    private long totalPageCount;

    public Page<MoneyIn> getMoneyIns() {
        return moneyIns;
    }

    public void setMoneyIns(Page<MoneyIn> moneyIns) {
        this.moneyIns = moneyIns;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
