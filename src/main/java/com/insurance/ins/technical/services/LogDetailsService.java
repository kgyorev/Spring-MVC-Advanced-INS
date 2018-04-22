package com.insurance.ins.technical.services;

import com.insurance.ins.technical.entites.LogDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogDetailsService {
    List<LogDetails> findByNameContains(String continentName);
    List<LogDetails> findAll();

    void deleteAllLogs();

}
