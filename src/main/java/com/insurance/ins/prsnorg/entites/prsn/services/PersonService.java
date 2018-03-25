package com.insurance.ins.prsnorg.entites.prsn.services;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import com.insurance.ins.utils.interfaces.FieldValueExists;

import java.util.List;

public interface PersonService extends FieldValueExists {
    List<Person> findAll();
    Person findById(Long id);
    Person create(Person client);
    Person edit(Person client);
    void deleteById(Long id);
}
