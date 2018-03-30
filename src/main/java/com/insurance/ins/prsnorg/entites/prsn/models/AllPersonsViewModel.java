package com.insurance.ins.prsnorg.entites.prsn.models;

import com.insurance.ins.prsnorg.entites.prsn.Person;
import org.springframework.data.domain.Page;

public class AllPersonsViewModel {
    private Page<Person> persons;

    private long totalPageCount;

    public Page<Person> getPersons() {
        return persons;
    }

    public void setPersons(Page<Person> persons) {
        this.persons = persons;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
