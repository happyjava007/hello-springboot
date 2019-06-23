# 使用spring initialize工具快速创建springboot项目 

IDEA专业版默认集成了此工具，eclipse或者vs code等可以自行搜索安装。如果不希望安装此插件，也可直接通过官网创建spring boot项目，然后下载到本地即可。官网地址如下：https://start.spring.io/

# 在IDEA使用spring initialize工具

创建项目的时候选择spring initialize

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb1860403?w=640&h=594&f=jpeg&s=20990)

设置maven group，项目名，jdk版本，包名等信息

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb1949de7?w=640&h=594&f=jpeg&s=21939)



选择依赖

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb1b4a948?w=640&h=594&f=jpeg&s=23199)



这里我选择core下的Lombok和web下的web。

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb1c8b907?w=181&h=178&f=jpeg&s=2964)



Lombok是一个简化pojo的通用方法的插件。Web是springboot的web核心依赖啦。然后一直下一步即可完成项目的创建了。

# 自动引入的依赖

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb190240a?w=640&h=359&f=jpeg&s=31128)



# 项目结构

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bdb1d2abf0?w=478&h=548&f=jpeg&s=23035)



SpringbootApplication是springboot项目的启动类。SpringbootApplicationTests是springboot的单元测试的一个类，这里先不关注它。

# 启动项目

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bddb979584?w=1240&h=356&f=jpeg&s=65537)



这里说明项目在8080端口启动了。通过浏览器访问该端口，出现以下页面说明springboot环境搭建成功

![img](https://user-gold-cdn.xitu.io/2019/6/12/16b4b8bde240f585?w=640&h=287&f=jpeg&s=22885)