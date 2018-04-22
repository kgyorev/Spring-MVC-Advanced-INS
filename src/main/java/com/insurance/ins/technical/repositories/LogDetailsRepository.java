package com.insurance.ins.technical.repositories;

import com.insurance.ins.technical.entites.LogDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LogDetailsRepository extends JpaRepository<LogDetails,Long> {

    public List<LogDetails> findByNameContains(String continentName);

}
