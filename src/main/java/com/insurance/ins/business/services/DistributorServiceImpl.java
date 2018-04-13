package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.repositories.DistributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DistributorServiceImpl implements DistributorService {

    private final DistributorRepository distributorRepository;

    @Autowired
    public DistributorServiceImpl(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Override
    public List<Distributor> findAll() {
        return this.distributorRepository.findAll();
    }

    @Override
    public Distributor findById(Long id) {
        return this.distributorRepository.getOne(id);
    }

    @Override
    public void deleteById(Long id) {
        this.distributorRepository.deleteById(id);
    }


    @Override
    public Distributor create(Distributor distributor) {
        return null;
    }

    @Override
    public Distributor edit(DistributorModel distributor) {
        return null;
    }



}
