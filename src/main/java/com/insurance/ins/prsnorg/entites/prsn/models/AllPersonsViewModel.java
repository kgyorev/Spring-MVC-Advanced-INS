package com.insurance.ins.prsnorg.entites.prsn.models;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import org.springframework.data.domain.Page;

public class AllPersonsViewModel {
    private Page<Person> persons;

    private long totalPageCount;

    public Page<Person> getCompanies() {
        return persons;
    }

    public void setCompanies(Page<Person> persons) {
        this.persons = persons;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
