package cn.happyjava.transactionpropagation.repo;

import cn.happyjava.transactionpropagation.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author happy
 */
public interface UserRepo extends CrudRepository<UserEntity, Integer> {


}
