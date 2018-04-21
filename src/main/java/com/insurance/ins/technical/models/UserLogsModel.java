package com.insurance.ins.technical.models;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserLogsModel {
    private Long id;
    private String Modification;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate modificationDate;
    private Long contractId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModification() {
        return Modification;
    }

    public void setModification(String modification) {
        Modification = modification;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
