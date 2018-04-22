package com.insurance.ins.technical.services;

import com.insurance.ins.technical.entites.LogDetails;
import com.insurance.ins.technical.repositories.LogDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LogDetailsServiceImpl implements LogDetailsService {
    private final LogDetailsRepository logDetailsRepository;

    @Autowired
    public LogDetailsServiceImpl(LogDetailsRepository logDetailsRepository) {
        this.logDetailsRepository = logDetailsRepository;
    }

    @Override
    public List<LogDetails> findByNameContains(String continentName) {
        return this.logDetailsRepository.findByNameContains(continentName);
    }

    @Override
    public List<LogDetails> findAll() {
        return this.logDetailsRepository.findAll();
    }

    @Override
    public void deleteAllLogs() {
        this.logDetailsRepository.deleteAll();
    }
}
