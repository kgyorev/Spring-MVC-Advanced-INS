package com.insurance.ins.technical.services;

import com.insurance.ins.technical.entites.Role;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.models.binding.UserEditBindingModel;
import com.insurance.ins.models.binding.UserRegisterBindingModel;
import com.insurance.ins.models.service.UserServiceModel;
import com.insurance.ins.technical.repositories.RoleRepository;
import com.insurance.ins.technical.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by K on 20.3.2018 г..
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//                new HashSet<>());
//        Set<SimpleGrantedAuthority> roles = user.getAuthorities().stream().map(r -> new SimpleGrantedAuthority("ROLE_"+r.getAuthority())).collect(Collectors.toSet());
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
//                roles);
        Collection<? extends GrantedAuthority> auth = user.getAuthorities();
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, (Set<Role>) auth);
//        return userDetails;
    }


    @Override
    public User register(UserRegisterBindingModel userRegisterBindingModel) {
        User user = DTOConvertUtil.convert(userRegisterBindingModel, User.class);
        user.setPassword(this.encoder.encode(userRegisterBindingModel.getPassword()));
        List<User> allUsers = this.userRepository.findAll();
        if(allUsers.size()==0){
            this.setRoles("ADMIN",user);
        } else{
            this.setRoles("USER",user);
        }

//        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
//       HashSet<Role> authorities = (HashSet<Role>) user.getAuthorities();


//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        user.setAuthorities(roles);



//        this.roleRepository.save(role);

        return this.userRepository.saveAndFlush(user);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<UserServiceModel> allUsers = DTOConvertUtil.convert(this.userRepository.findAll(), UserServiceModel.class);
        return allUsers ;
    }

    @Override
    public void setRoles(String profile,User user) {
        user.setProfile(profile);
        if(profile.equals("ADMIN")){
            Set<Role> allRolse = new HashSet<>(this.roleRepository.findAll());
            user.setAuthorities(allRolse);
        } else if(profile.equals("MODERATOR")){
            Set<Role> profile_moderator = this.roleRepository.findAll().stream().filter(a -> !a.getAuthority().equals("ROLE_ADMIN")).collect(Collectors.toSet());
            user.setAuthorities(profile_moderator);
        } else {
            Role role_user = this.roleRepository.findByAuthority("ROLE_USER");
            Set<Role> roles = new HashSet<Role>() {{
                add(role_user);
            }};
            user.setAuthorities(roles);
        }

    }

    @Override
    public User findById(Long id) {
         return this.userRepository.getOne(id);
    }

    @Override
    public void edit(@Valid UserEditBindingModel userEditBindingModel) {
        User user = DTOConvertUtil.convert(userEditBindingModel, User.class);
        user.setPassword(this.encoder.encode(userEditBindingModel.getPassword()));
        this.setRoles(user.getProfile(),user);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }
}
