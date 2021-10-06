# 1 前言
工作了很多年，都没有自己的一个项目脚手架，所以说，前阵子就准备搞一个自己的`Spring Cloud微服务`的架构。Spring Cloud 官网，`2021-07-06` 发布了**Hoxton.SR12** 这个版本， **本来想使用 `Hoxton.SR12`这个Spring Cloud版本，查了一些资料，发现基于这个版本，好用的微服务架构体系并且开源的项目不是很多，可能是这个版本刚出来两三个月，就自己折腾了一个基础架构**。在进行依赖管理的过程中，走了不少坑，各种jar冲突或者版本不兼容等等，这里总结记录下，防止以后再次踩坑。


为了搭建自己的脚手架，方便以后项目的管理和维护，现在开发了这一套基础架构项目，该项目基于 `Spring Boot`+ `Spring Cloud` + `MyBatis-Plus`，开发这个基础设施项目，专门用来给子项目继承或者子项目可以单独直接依赖该项目的某个小功能模块来使用。


为了提高项目的开发效率，降低项目的维护成本，建议直接继承使用该基础设施项目，避免重复造轮子。`该项目后续也会继续迭代，来完善该基础功能架构。`

# 2 核心组件版本号

**核心组件版本号如下：**

| 组件名称 | 版本号 |
|:---:|:---:|
| Spring Version | 5.2.15.RELEASE |
| Spring Boot Version | 2.3.12.RELEASE |
| Spring Cloud Version | Hoxton.SR12 |
| Spring Cloud Openfeign | 2.2.9.RELEASE |
| Spring Cloud Alibaba Version | 2.2.1.RELEASE |
| Spring Data Redis Version | 2.4.13 |
| Jedis Version | 3.3.0 |
| Redisson Version | 3.16.1 |
| MyBatis-Plus Version | 3.3.2 |
| HikariCP Version | 3.4.5 |
| MySQL Connector Version | 8.0.22 |
| Xxl-Job | 2.3.0 |
| JDK | 1.8 |


# 3 该微服务架构提供那些组件功能

**该项目主要包括以下功能模块：**
* `统一管理项目依赖，核心依赖的版本控制`
* `缓存管理以及分布式锁的处理`
* `预警通知功能`
* `异常管理`
* `限流Api管理`
* `Mock Server管理`
* `消息中间件MQ管理`
* `操作日志管理`
* `定时任务管理`
* `Swagger-Ui管理`
* `工具类管理`

# 4 待完善的功能
* redis目前只支持单机，后续在考虑集群或哨兵部署
* 集成网关服务
* 集成服务熔断降级
* 集成分布式事务管理模块
* 集成Sentry
* 集成ELK管理模块

  如何快速的定位跟踪问题？那么，`SkyWalking` + `ELK` + `Sentry` + `钉钉预警`，在做一些核心数据的统计报表，对于一般的项目开发，应该足够用了

# 5 基础项目下载地址

* **GitHub下载地址：** [smilehappiness-architecture](https://github.com/smilehappinessli/smilehappiness-architecture.git)
* **Gitee下载地址：** [smilehappiness-architecture](https://gitee.com/smilehappiness/smilehappiness-architecture)


**详细的使用教程，可以参考我另外的博客，写的非常详细：**
* [Spring Cloud 微服务基础功能架构来啦](https://blog.csdn.net/smilehappiness/article/details/120623974)
* [基于Spring Cloud 的微服务架构脚手架，满满的干货来啦](https://blog.csdn.net/smilehappiness/article/details/120624307)