package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.repositories.DistributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DistributorServiceImpl implements DistributorService {

    private final DistributorRepository distributorRepository;

    @Autowired
    public DistributorServiceImpl(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Override
    public List<Distributor> findAll() {
        return this.distributorRepository.findAll();
    }

    @Override
    public Distributor findById(Long id) {
        return this.distributorRepository.getOne(id);
    }

//    @Override
//    public Distributor create(Distributor distributor) {
//        String hash = bCrypt(distributor.getPasswordHash());
//        distributor.setPasswordHash(hash);
//
//        return this.distributorRepository.save(distributor);
//    }

//    @Override
//    public Distributor edit(Distributor distributor) {
//        String hash = bCrypt(distributor.getPasswordHash());
//        distributor.setPasswordHash(hash);
//        return this.distributorRepository.save(distributor);
//    }

    @Override
    public void deleteById(Long id) {
        this.distributorRepository.deleteById(id);
    }

//    @Override
//    public boolean authenticate(String username, String password) throws SQLException
//    {
//        String query = "Select u.password_hash from distributors u where u.username = ?";
//        PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
//        String hash = bCrypt(password);
//        pstmt.setString(1, username);
//        ResultSet resultSet = pstmt.executeQuery();
//
//         if(resultSet.next()){
//            String pass_db = resultSet.getString("password_hash");
//            if(bCryptCheck(password,pass_db))
//                return true;
//            else
//                return false;
//        }
//
//        else
//           return false;
//
//    }

    @Override
    public Distributor login(String username, String password) {
        throw new UnsupportedOperationException("login Operation not implemented");
    }

    @Override
    public Distributor register(String username, String password, String fullName) {
        throw new UnsupportedOperationException("register Operation not implemented");
    }

    @Override
    public void setPassword(String username, String newPassword) {
        throw new UnsupportedOperationException("setPassword Operation not implemented");
    }
//    private static String bCrypt(String password) {
//        String result = BCrypt.hashpw(password,BCrypt.gensalt());
//        return result;
//    }
//    private static Boolean bCryptCheck(String password1, String password2) {
//        boolean result = BCrypt.checkpw(password1,password2);
//        return result;
//    }

}
