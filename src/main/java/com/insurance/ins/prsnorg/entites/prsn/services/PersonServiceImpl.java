package com.insurance.ins.prsnorg.entites.prsn.services;

import com.insurance.ins.business.repositories.DistributorRepository;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.prsnorg.entites.prsn.models.EditPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.SearchPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.reposiotries.PersonRepository;
import com.insurance.ins.utils.DTOConvertUtil;
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
public class PersonServiceImpl implements PersonService {


    private final PersonRepository personRepository;
    private final DistributorRepository distributorRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, DistributorRepository distributorRepository) {
        this.personRepository = personRepository;
        this.distributorRepository = distributorRepository;
    }

    @Override
    public List<Person> findAll() {
        return this.personRepository.findAll();
    }

    @Override
    public AllPersonsViewModel findAllByPage(Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setPersons(this.personRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllPersonsViewModel findAllByEgnAndFullName(String egn, String fullName, Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setPersons(this.personRepository.findAllByEgnAndFullNameContains(egn,fullName,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllPersonsViewModel findAllByFullName(String fullName, Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setPersons(this.personRepository.findAllByFullNameContains(fullName,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllPersonsViewModel findAllByEgn(String egn, Pageable pageable) {
        AllPersonsViewModel viewModel = new AllPersonsViewModel();

        viewModel.setPersons(this.personRepository.findAllByEgn(egn,pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.personRepository.count() / size;
    }
    @Override
    public Person findById(Long id) {
        return this.personRepository.findById(id).orElse(null);
    }

    @Override
    public Person create(Person person) {
        return this.personRepository.saveAndFlush(person);

    }

    @Override
    public Person edit(Person person, EditPersonModel personModel) {
        Person personEdit = DTOConvertUtil.convert(personModel, Person.class);
        personEdit.setEgn(person.getEgn());
        return this.personRepository.saveAndFlush(person);
    }

    @Override
    public void deleteById(Long id) {
        this.personRepository.deleteById(id);
    }

    @Override
    public AllPersonsViewModel searchPerson(SearchPersonModel searchPersonModel, Pageable pageable) {

        String egn = searchPersonModel.getEgn();
        String fullName = searchPersonModel.getFullName();

        AllPersonsViewModel personAll;

        if (!egn.equals("") && !fullName.equals("")) {
            personAll = this.findAllByEgnAndFullName(egn, fullName, pageable);
        } else if (!egn.equals("") && fullName.equals("")) {
            personAll = this.findAllByEgn(egn, pageable);
        } else if (egn.equals("")&& !fullName.equals("")) {
            personAll = this.findAllByFullName(fullName, pageable);
        } else {
            personAll = this.findAllByPage(pageable);
        }
        return personAll;
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
