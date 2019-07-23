## 前言

缓存，在开发中是非常常用的。在高并发系统中，如果没有缓存，纯靠数据库来扛，那么数据库压力会非常大，搞不好还会出现宕机的情况。本篇文章，将会带大家学习Spring Cache缓存框架。

## 创建SpringBoot工程

通过Spring initialise快速创建SpringBoot工程，可以参考：http://blog.happyjava.cn/articles/7.html



## 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```



## EnableCaching开启缓存

```java
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 默认就是这种配置，可以不写
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

}
```

在SpringBoot中，默认就是ConcurrentMapCacheManager的缓存方式，不写也是可以的。这里可以通过CacheManager配置不同的缓存实现方式，比如redis，EHCACHE等。这个在下个章节再讲解。

ConcurrentMapCacheManager还有一个不定参数的重载构造方法，

![](http://file.happyjava.cn/picgo/20190721233127.png)

它接收的是cacheName入参，如果设置了缓存名字，那么后续的方法就只能使用在这里设置的缓存，否则会抛出异常。如果是无参构造方法，那么它是一个可变的缓存管理器。

![](http://file.happyjava.cn/picgo/20190721233454.png)



## Cacheable注解

Cacheable注解是用来设置缓存的。常用注解如下：

#### value或者cacheNames

指定使用的缓存，其实也就是上面说到的cacheName。如果是无参的ConcurrentMapCacheManager，那么这里可以根据自己的用途等因素自定义即可。



#### key

缓存的key，就跟Map一样，是操作缓存的键。

这里接受的是 **SpEL**表达式。关于SpEL表达式，下面做个简要说明：

有如下方法：

```java
@CachePut(value = "listUsers", key = "#username")
public List<String> updateCache(String username) {
    System.out.println("执行了updateCache方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

可以通过 #paramName 的方式获得入参

还有一个是root对象，用法如下：

```java
@CachePut(value = "listUsers", key = "#root.methodName+#username")
public List<String> updateCache(String username) {
    System.out.println("执行了updateCache方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

其实没必要记住，通过IDEA的智能提示灵活使用即可：

![](http://file.happyjava.cn/picgo/20190721234614.png)

各个参数代表的意思，相信大家一目了然。



#### condition

缓存控制条件，如果使用了该字段，那么只有结果为true时，才会缓存结果。

```java
@Cacheable(value = "listUsers", condition = "#username.length()>5")
public List<String> listUsers(String username) {
    System.out.println("执行了listUsers方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

下面贴出一个设置缓存的例子：

```java
@Cacheable(value = "listUsers", key = "#root.methodName+#username", condition = "#username.equals('Happyjava')")
public List<String> listUsers(String username) {
    System.out.println("执行了listUsers方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

只有当入参username等于"Happyjava"的时候，才会缓存结果，可以通过是否多次打印："执行了listUsers方法"来判断。



## CachePut注解

相信HTTP协议熟悉的朋友一看名字就知道这个注解是干嘛用的了。我们可以通过CachePut注解来更新缓存。其常用注解与Cacheable是一致的。下面给出一个更新缓存的例子：

```java
@CachePut(value = "listUsers", key = "#username", condition = "#username.equals('Happyjava')")
public List<String> updateCache(String username) {
    System.out.println("执行了updateCache方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

我们可以写一个value和key与之对应的Cacheable注解进行测试：

```java
@Cacheable(value = "listUsers", key = "#username", condition = "#username.equals('Happyjava')")
public List<String> listUsers(String username) {
    System.out.println("执行了listUsers方法");
    return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
}
```

测试预期的结果是：调用了CachePut方法后，Cacheable方法会返回一个新的结果。



## CacheEvict注解

这是一个删除注解。常用参数除了上面两个注解列出的三个之外，还有一个allEntries，这是一个布尔类型的参数，默认为false，其意思是“是否删除所有缓存”。在false的情况下，只是删除与key相对应的缓存，如果为true，则会删除所有缓存（当然是对应的value下的）。

```java
@CacheEvict(value = "listUsers", key = "#username")
public void deleteCache(String username) {
    System.out.println("执行了deleteCache方法");
}

@CacheEvict(value = "listUsers", allEntries = true)
public void deleteAllCache() {

}
```



## 总结

当然，这只是Spring cache的一个快速上手示例，其实我们更多时候不是这么使用的。在实际项目中，更多是配合redis进行使用的，这个放在下篇文章讲解吧（其实也就是一些配置的事情）



## 参考代码

https://github.com/Happy4Java/hello-springboot