package com.insurance.ins.prsnorg.entites.prsn.services;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.prsnorg.entites.prsn.reposiotries.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Primary
@Transactional
public class PersonServiceJpaImpl implements PersonService {


    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceJpaImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAll() {
        return this.personRepository.findAll();
    }

    @Override
    public AllPersonsViewModel findAllByPage(Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setCompanies(this.personRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllPersonsViewModel findAllByEgnOrFullNameIsLike(String egn, String fullName, Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setCompanies(this.personRepository.findAllByEgnOrFullNameIsLike(egn,fullName,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.personRepository.count() / size;
    }
    @Override
    public Person findById(Long id) {
        return this.personRepository.getOne(id);
    }

    @Override
    public Person create(Person person) {
        return this.personRepository.saveAndFlush(person);
    }

    @Override
    public Person edit(Person person) {
        return this.personRepository.save(person);
    }

    @Override
    public void deleteById(Long id) {
        this.personRepository.deleteById(id);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        Assert.notNull(fieldName,"Can't be null");

        if (!fieldName.equals("egn")) {
            throw new UnsupportedOperationException("Field name not supported");
        }

        if (value == null) {
            return false;
        }

        return this.personRepository.existsByEgn(value.toString());
    }
}