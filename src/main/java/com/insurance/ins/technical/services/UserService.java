package com.insurance.ins.technical.services;

import com.insurance.ins.technical.entites.User;
import com.insurance.ins.models.binding.UserEditBindingModel;
import com.insurance.ins.models.binding.UserRegisterBindingModel;
import com.insurance.ins.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by K on 7.3.2018 г..
 */
@Service
public interface UserService extends UserDetailsService {
    User register(UserRegisterBindingModel userRegisterBindingModel);


    List<UserServiceModel> getAllUsers();

    void setRoles(String profile, User user);

    User findById(Long id);

    void edit(@Valid UserEditBindingModel userEditBindingModel);

    void delete(Long id);
}
