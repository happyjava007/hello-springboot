package cn.happyjava.transactionpropagation;

import cn.happyjava.transactionpropagation.entity.UserEntity;
import cn.happyjava.transactionpropagation.repo.UserRepo;
import cn.happyjava.transactionpropagation.service.UserService;
import cn.happyjava.transactionpropagation.service.UserService2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionPropagationApplicationTests {

    @Autowired
    UserService2 userService2;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Test
    public void insertBatchTest() {
        userService2.inserBatch();
    }

    @Test
    public void insertWithoutTxTest() {
        userService2.insertWithoutTx();
    }

    @Test
    public void insertWithTxTest() {
        userService2.insertWithTx();
    }

    @Test
    public void insertMainTest() {
        userService2.insertMain();
    }

}
