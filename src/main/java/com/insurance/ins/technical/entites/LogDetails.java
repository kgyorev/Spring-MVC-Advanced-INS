package com.insurance.ins.technical.entites;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by K on 12.3.2018 г..
 */
@Entity
public class LogDetails {

    private Long id;
    private String name;
    private Date time;
    private String operation;
    private String modifiedTable;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column
    public Date getTime() {
        return time;
    }

    public void setTime(Date createdOn) {
        this.time = createdOn;
    }
    @Column
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    @Column
    public String getModifiedTable() {
        return modifiedTable;
    }

    public void setModifiedTable(String modifiedTable) {
        this.modifiedTable = modifiedTable;
    }
    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
