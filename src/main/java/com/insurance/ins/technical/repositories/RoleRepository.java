package com.insurance.ins.technical.repositories;

import com.insurance.ins.technical.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by K on 7.3.2018 г..
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByAuthority(String authority);

}
