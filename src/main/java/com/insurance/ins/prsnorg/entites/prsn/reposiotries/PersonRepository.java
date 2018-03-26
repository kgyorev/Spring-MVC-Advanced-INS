package com.insurance.ins.prsnorg.entites.prsn.reposiotries;


import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends  JpaRepository<Person, Long> {

    Boolean existsByEgn(String egn);
    Page<Person> findAllByEgnOrFullNameIsLike(String egn, String fullName, Pageable pageable);
}



