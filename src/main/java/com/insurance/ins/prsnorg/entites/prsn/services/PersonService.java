package com.insurance.ins.prsnorg.entites.prsn.services;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService extends FieldValueExists {
    List<Person> findAll();
   AllPersonsViewModel findAllByPage(Pageable pageable);
    AllPersonsViewModel findAllByEgnOrFullNameIsLike(String egn, String fullName, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(12);
    }
    long getTotalPages(int size);
    Person findById(Long id);
    Person create(Person client);
    Person edit(Person client);

    void deleteById(Long id);
}