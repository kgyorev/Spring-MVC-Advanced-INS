package com.insurance.ins.technical.entites;

import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Audited
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String authority;


    public Role() {
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }


}
