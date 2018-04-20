package com.insurance.ins.prsnorg.entites.prsn.services;

import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.prsnorg.entites.prsn.models.EditPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.SearchPersonModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService extends FieldValueExists {
    List<Person> findAll();

    AllPersonsViewModel findAllByPage(Pageable pageable);

    AllPersonsViewModel findAllByEgnAndFullName(String egn, String fullName, Pageable pageable);

    AllPersonsViewModel findAllByFullName(String fullName, Pageable pageable);

    AllPersonsViewModel findAllByEgn(String egn, Pageable pageable);

    default long getTotalPages() {
        return getTotalPages(10);
    }

    long getTotalPages(int size);

    Person findById(Long id);

    Person create(Person client);

    Person edit(Person client, EditPersonModel personModel);

    void deleteById(Long id);

    AllPersonsViewModel searchPerson(SearchPersonModel searchPersonModel, Pageable pageable);
}
