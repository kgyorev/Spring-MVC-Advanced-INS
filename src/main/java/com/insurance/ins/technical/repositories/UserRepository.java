package com.insurance.ins.technical.repositories;

import com.insurance.ins.technical.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by K on 7.3.2018 г..
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByUsername(String username);
    User findByUsername(String username);
}
