package com.insurance.ins.technical.services;


import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.technical.models.UserServiceModel;
import com.insurance.ins.technical.entites.AuditEnversInfo;
import com.insurance.ins.technical.entites.Role;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.models.*;
import com.insurance.ins.technical.repositories.RoleRepository;
import com.insurance.ins.technical.repositories.UserRepository;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, FieldValueExists {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder, ContractService contractService, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.entityManager = entityManager;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<? extends GrantedAuthority> auth = user.getAuthorities();
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, (Set<Role>) auth);
    }


    @Override
    public User register(UserModel userRegisterBindingModel) {
        User user = DTOConvertUtil.convert(userRegisterBindingModel, User.class);
        user.setPassword(this.encoder.encode(userRegisterBindingModel.getPassword()));
        List<User> allUsers = this.userRepository.findAll();
        if (allUsers.size() == 0) {
            this.setRoles("ADMIN", user);
        } else {
            this.setRoles("USER", user);
        }
        return this.userRepository.saveAndFlush(user);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<UserServiceModel> allUsers = DTOConvertUtil.convert(this.userRepository.findAll(), UserServiceModel.class);
        return allUsers;
    }

    @Override
    public void setRoles(String profile, User user) {
        user.setProfile(profile);
        if (profile.equals("ADMIN")) {
            Set<Role> allRolse = new HashSet<>(this.roleRepository.findAll());
            user.setAuthorities(allRolse);
        } else if (profile.equals("MODERATOR")) {
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
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public void edit(User userOld, EditUserModel userEditBindingModel) {
        User user = DTOConvertUtil.convert(userEditBindingModel, User.class);
        user.setUsername(userOld.getUsername());
        user.setPassword(this.encoder.encode(userEditBindingModel.getPassword()));
        this.setRoles(user.getProfile(), user);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public AllUsersViewModel searchUser(SearchUserModel searchUserModel, Pageable pageable) {
        String searchBy = searchUserModel.getSearchBy();
        String referenceCriteriaStr = searchUserModel.getCriteria();
        if (referenceCriteriaStr == null || referenceCriteriaStr.equals("")) {
            return this.findAllByPage(pageable);
        }
        switch (searchBy) {
            case "username":
                return this.findAllByUsername(searchUserModel.getCriteria(), pageable);
            default:
            {
                Long idUser = 0L;
                try{
                    idUser = Long.valueOf(searchUserModel.getCriteria());
                }
                catch(Exception e){
                }
                return this.findAllById(idUser, pageable);
            }

        }
    }


    @Override
    public AllUsersViewModel findAllById(Long id, Pageable pageable) {
        AllUsersViewModel viewModel = new AllUsersViewModel();
        viewModel.setUsers(this.userRepository.findAllById(id, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());
        return viewModel;
    }

    @Override
    public AllUsersViewModel findAllByUsername(String username, Pageable pageable) {

        AllUsersViewModel viewModel = new AllUsersViewModel();

        viewModel.setUsers(this.userRepository.findAllByUsername(username, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }


    @Override
    public AllUsersViewModel findAllByPage(Pageable pageable) {
        AllUsersViewModel viewModel = new AllUsersViewModel();
        viewModel.setUsers(this.userRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());
        return viewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.userRepository.count() / size;
    }

    @Override
    public List<UserLogsModel> searchUserLog(String searchBy, String criteria) {
        User user = null;
        if (searchBy.equals("userId")) {
            Long idUser = 0L;
            try{
                idUser = Long.valueOf(criteria);
            }
            catch(Exception e){

            }
            user = this.findById(idUser);
        } else {
            user = userRepository.findByUsername(criteria);
        }
        List<UserLogsModel> revLs = new ArrayList<UserLogsModel>();
        if (user != null) {
            String username = user.getUsername();
            AuditReader auditReader = AuditReaderFactory.get(this.entityManager);
            AuditQuery query = auditReader.createQuery()
                    .forRevisionsOfEntity(Contract.class, false, true);
            query.add(AuditEntity.revisionProperty("userId").eq(username));
            List<Object> resultList = query.getResultList();
            for (Object rev : resultList) {
                UserLogsModel userLogsModel = new UserLogsModel();
                Object[] revision = (Object[]) rev;
                Contract revisionEntity = (Contract) revision[0];
                AuditEnversInfo revisionInfo = (AuditEnversInfo) revision[1];
                RevisionType revisionType = (RevisionType) revision[2];
                Integer id1 = revisionInfo.getId();

                userLogsModel.setId(Long.valueOf(id1));
                userLogsModel.setContractId(revisionEntity.getId());
                Date revisionDate = auditReader.getRevisionDate(id1);
                LocalDate revisionDateLocalDate = revisionDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                userLogsModel.setModificationDate(revisionDateLocalDate);
                userLogsModel.setModification(revisionType.toString());
                revLs.add(userLogsModel);
            }
        }
        return revLs;
    }


    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        Assert.notNull(fieldName, "Can't be null");

        if (!fieldName.equals("username") && !fieldName.equals("id")) {
            throw new UnsupportedOperationException("Field name not supported");
        }

        if (value == null) {
            return false;
        }
        if (fieldName.equals("username")) {
            return this.userRepository.existsByUsername(value.toString());
        } else {
            return this.userRepository.existsById(Long.valueOf(value.toString()));
        }

    }
}

