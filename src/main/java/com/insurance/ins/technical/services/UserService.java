package com.insurance.ins.technical.services;

import com.insurance.ins.models.service.UserServiceModel;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.models.AllUsersViewModel;
import com.insurance.ins.technical.models.EditUserModel;
import com.insurance.ins.technical.models.SearchUserModel;
import com.insurance.ins.technical.models.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by K on 7.3.2018 г..
 */
@Service
public interface UserService extends UserDetailsService {
    User register(UserModel userRegisterBindingModel);


    List<UserServiceModel> getAllUsers();

    void setRoles(String profile, User user);

    User findById(Long id);

    void edit(User userOld, EditUserModel userEditBindingModel);

    void delete(Long id);



    AllUsersViewModel searchUser(SearchUserModel searchUserModel, Pageable pageable);




    default long getTotalPages() {
        return getTotalPages(10);
    }

    AllUsersViewModel findAllById(Long id, Pageable pageable);

    AllUsersViewModel findAllByUsername(String username, Pageable pageable);

    AllUsersViewModel findAllByPage(Pageable pageable);

    long getTotalPages(int size);



}
