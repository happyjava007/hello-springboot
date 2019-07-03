## 前言

JWT(json web token)的无状态鉴权方式，越来越流行。配合SpringSecurity+SpringBoot，可以实现优雅的鉴权功能。

关于SpringBoot+ Security的讲解，可以参考我之前的文章：https://www.toutiao.com/i6704647082659021319/ 

为了减少重复造轮子的工作量，方便大家复制和参考，我把一个完整的SpringBoot+JWT+SpringSecurity+Mybatis-Plus开发的项目，放到本人的github上，方便自己的同时也方便他人。



## 源码获取

github地址，参考文末即可。



## 项目实现的功能

1、整合了好用方便的Mybatis-plus

2、整合了JWT

3、整合了Spring Security



## 简单演示

配置拦截和放行的路径

![](http://file.happyjava.cn/picgo/20190703235944.png)



#### 未登录请求

![](http://file.happyjava.cn/picgo/20190704000049.png)

会被拦截返回401。这个返回的内容用户可以自定义即可



#### 登录

![](http://file.happyjava.cn/picgo/20190704000137.png)



#### 登录成功后访问需要登录的接口

![](http://file.happyjava.cn/picgo/20190704000215.png)

这里成功请求到了数据。



## 通过注解获取当前 登录的用户

![](http://file.happyjava.cn/picgo/20190704000259.png)

![](http://file.happyjava.cn/picgo/20190704000324.png)



## 项目部署

#### 数据库准备

建立数据库test，建表如下：

```
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
```

建表之后，自行插入用户名密码。



## 修改配置文件applicatoin.properties

把数据密码等配置修改正确

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# 需要手写mapper打开此配置
mybatis-plus.mapper-locations=classpath:mappers/*.xml
# jwt的密钥
jwt.secret.key=happyjava1234214214asfasfasfasdf
# jwt过期时间
jwt.token.expired=360000
```

也可自定义jwt的加密密钥和token过期时间



## 启动项目

启动项目即可通过接口进行测试



## 源码地址

https://github.com/Happy4Java/hello-springboot

![](http://file.happyjava.cn/picgo/20190704000731.png)

