package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;

import java.util.List;

public interface DistributorService {
    List<Distributor> findAll();
    Distributor findById(Long id);
//    Distributor create(Distributor distributor);
//    Distributor edit(Distributor distributor);
    void deleteById(Long id);
//    boolean authenticate(String username, String password) throws SQLException;
    Distributor login(String username, String password);
    Distributor register(String username, String password, String fullName);
    void setPassword(String username, String newPassword);
}
