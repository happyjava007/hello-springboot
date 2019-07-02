package cn.happyjava.transactionpropagation.service;

import cn.happyjava.transactionpropagation.entity.UserEntity;
import cn.happyjava.transactionpropagation.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService2 {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepo userRepo;

    @Transactional
    public void inserBatch() {
        UserEntity user = new UserEntity();
        user.setUsername("初次调用");
        user.setPassword("123456");
        userRepo.save(user);
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                throw new RuntimeException();
            }
            userService.insert();
        }
    }

    public void insertWithoutTx() {
        userService.insert();
    }

    @Transactional
    public void insertWithTx() {
        userService.insert();
    }

    @Transactional
    public void insertMain() {
        UserEntity user = new UserEntity();
        user.setUsername("主事务");
        user.setPassword("apsdfk");
        userRepo.save(user);

        // 内嵌事务
        try {
            userService.insert();
        } catch (Exception e) {
            System.out.println("内嵌事务回滚");
        }

        throw new RuntimeException();

    }

}
