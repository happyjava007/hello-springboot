## 前言

在开发中，相信大家都使用过Spring的事务管理功能。那么，你是否有了解过，Spring的事务传播行为呢？

Spring中，有7种类型的事务传播行为。事务传播行为是Spring框架提供的一种事务管理方式，它不是数据库提供的。不知道大家是否听说过“不要在service事务方法中嵌套事务方法，这样会提交多个事务”的说法，其实这是不准确的。了解了事务传播行为之后，相信你就会明白！



## Spring中七种事务传播行为

事务的传播行为，默认值为 Propagation.REQUIRED。可以手动指定其他的事务传播行为，如下：

- Propagation.REQUIRED

如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。

- Propagation.SUPPORTS

如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。

- Propagation.MANDATORY

如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。

- Propagation.REQUIRES_NEW

重新创建一个新的事务，如果当前存在事务，延缓当前的事务。

- Propagation.NOT_SUPPORTED

以非事务的方式运行，如果当前存在事务，暂停当前的事务。

- Propagation.NEVER

以非事务的方式运行，如果当前存在事务，则抛出异常。

- Propagation.NESTED

如果没有，就新建一个事务；如果有，就在当前事务中嵌套其他事务。



## 准备工作

数据库表：

```mysql
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

一个整合了Spring Data JPA的SpringBoot工程，这里就不多说了。



##  REQUIRED(默认的事务传播行为)

默认的事务传播行为是Propagation.REQUIRED，也就是说：如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。

![](http://file.happyjava.cn/picgo/20190625212656.png)

下面，我们就验证下前面说的“不要循环嵌套事务方法”的问题：

现在有两个Service，如下：

#### UserService.java

```java
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Transactional(propagation = Propagation.REQUIRED)
    public void insert() {
        UserEntity user = new UserEntity();
        user.setUsername("happyjava");
        user.setPassword("123456");
        userRepo.save(user);
    }


}
```

这里很简单，就一个insert插入用户的方法。



#### UserService2.java

```java
@Service
public class UserService2 {

    @Autowired
    private UserService userService;


    @Transactional
    public void inserBatch() {
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                throw new RuntimeException();
            }
            userService.insert();
        }
    }

}
```

注入UserService，循环十次调用参数方法。并且第十次抛出异常。调用inserBatch方法，查看结果：

```java
@Test
public void insertBatchTest() {
    userService2.inserBatch();
}
```

结果如下：

![](http://file.happyjava.cn/picgo/20190625213402.png)

数据库中没有记录：

![](http://file.happyjava.cn/picgo/20190625213428.png)

这也证明了“如果当前存在事务，则加入该事务”的概念。如果以后还碰到有人说不要循环嵌套事务的话，可以叫他回去好好看看Spring的事务传播行为。



## SUPPORTS

如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。也就是说，该模式是否支持事务，看调用它的方法是否有事务支持。测试代码如下：

#### UserService

```java
@Transactional(propagation = Propagation.SUPPORTS)
public void insert() {
    UserEntity user = new UserEntity();
    user.setUsername("happyjava");
    user.setPassword("123456");
    userRepo.save(user);
    throw new RuntimeException();
}
```

#### UserService2

```java
public void insertWithoutTx() {
    userService.insert();
}
```

调用的方法没有开启事务，运行结果：

![](http://file.happyjava.cn/picgo/20190625214428.png)

![](http://file.happyjava.cn/picgo/20190625214348.png)

运行报错了，但是数据却没有回滚掉。说明了insert方法是没有在事务中运行的。



## MANDATORY

如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。mandatory中文是强制性的意思，表明了被修饰的方法，一定要在事务中去调用，否则会抛出异常。

#### UserService.java

```java
@Transactional(propagation = Propagation.MANDATORY)
public void insert() {
    UserEntity user = new UserEntity();
    user.setUsername("happyjava");
    user.setPassword("123456");
    userRepo.save(user);
}
```

UserService2.java

```java
public void insertWithoutTx() {
	userService.insert();
}
```

调用：

```java
@Test
public void insertWithoutTxTest() {
    userService2.insertWithoutTx();
}
```

运行结果：

![](http://file.happyjava.cn/picgo/20190625225611.png)

抛出了异常，提示没有存在的事务。



## REQUIRES_NEW

这个理解起来可能会比较绕，官方的解释是这样子的：

```
Create a new transaction, and suspend the current transaction if one exists.
```

大意就是：重新创建一个新的事务，如果当前存在事务，延缓当前的事务。这个延缓，或者说挂起，可能理解起来比较难，下面通过例子来分析：

#### UserService.java

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void insert() {
    UserEntity user = new UserEntity();
    user.setUsername("happyjava");
    user.setPassword("123456");
    userRepo.save(user);
}
```

这个insert方法的传播行为改为REQUIRES_NEW。

#### UserService2.java

```java
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
```

inserBatch拥有事务，然后后面循环调用的insert方法也有自己的事务。根据定义，inserBatch的事务会被延缓。具体表现就是：后面的10次循环的事务在每次循环结束之后都会提交自己的事务，而inserBatch的事务，要等循环方法走完之后再提交。但由于第10次循环会抛出异常，则inserBatch的事务会回滚，既数据库中不会存在：“初次调用”的记录：

测试代码：

```java
@Test
public void insertBatchTest() {
    userService2.inserBatch();
}
```

执行结果：

![](http://file.happyjava.cn/picgo/20190625230827.png)

![](http://file.happyjava.cn/picgo/20190625230842.png)

这种情况，符合开始说的“不要循环嵌套事务方法”的说话，当然是否需要循环嵌套，还是要看业务逻辑的。



## NOT_SUPPORTED

```
Execute non-transactionally, suspend the current transaction if one exists.
```

以非事务的方式运行，如果当前存在事务，暂停当前的事务。这种方式与REQUIRES_NEW有所类似，但是NOT_SUPPORTED修饰的方法其本身是没有事务的。这里就不做代码演示了。



## NEVER

以非事务的方式运行，如果当前存在事务，则抛出异常。

```java
@Transactional(propagation = Propagation.NEVER)
public void insert() {
    UserEntity user = new UserEntity();
    user.setUsername("happyjava");
    user.setPassword("123456");
    userRepo.save(user);
}
```

```java
@Transactional
public void insertWithTx() {
    userService.insert();
}
```

执行结果：

![](http://file.happyjava.cn/picgo/20190625231753.png)



## NESTED

如果没有事务，就新建一个事务；如果有，就在当前事务中嵌套其他事务。

这个也是理解起来比较费劲的一个行为。我们一步一步分析。

外围方法没有事务：这种情况跟REQUIRED是一样的，会新建一个事务。

外围方法如果存在事务：这种情况就会嵌套事务。所谓嵌套事务，大意就是，外围事务回滚，内嵌事务一定回滚，而内嵌事务可以单独回滚而不影响外围主事务和其他子事务。

由于本人使用Spring Data JPA 进行的演示代码，使用嵌套事务会提示：

```
org.springframework.transaction.NestedTransactionNotSupportedException: JpaDialect does not support savepoints - check your JPA provider's capabilities
```

搜索了下，hibernate似乎不支持这种事务传播方式。所以这里就不做演示了



## 总结

事务传播行为，在开发中可能不会特别的留意到它（更多时候，我们可能只是使用默认的方式），但是还是需要对其要有所理解。希望本篇文章能让大家明白Spring的7种事务传播行为。





