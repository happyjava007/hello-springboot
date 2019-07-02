package cn.happyjava.transactionpropagation.service;

import cn.happyjava.transactionpropagation.entity.UserEntity;
import cn.happyjava.transactionpropagation.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Transactional(propagation = Propagation.NESTED)
    public void insert() {
        UserEntity user = new UserEntity();
        user.setUsername("happyjava");
        user.setPassword("123456");
        userRepo.save(user);
        throw new RuntimeException();
    }

}
