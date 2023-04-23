@[TOC]

# 1 前言
工作了很多年，都没有自己的一个项目脚手架，所以说，前阵子就准备搞一个自己的`Spring Cloud微服务`的架构。Spring Cloud 官网，`2021-07-06` 发布了**Hoxton.SR12** 这个版本， **本来想使用 `Hoxton.SR12`这个Spring Cloud版本，查了一些资料，发现基于这个版本，好用的微服务架构体系并且开源的项目不是很多，可能是这个版本刚出来两三个月，就自己折腾了一个基础架构**。在进行依赖管理的过程中，走了不少坑，各种jar冲突或者版本不兼容等等，这里总结记录下，防止以后再次踩坑。


为了搭建自己的脚手架，方便以后项目的管理和维护，现在开发了这一套基础架构项目，该项目基于 `Spring Boot`+ `Spring Cloud` + `MyBatis-Plus`+`Spring Cloud Alibaba`，开发这个基础设施项目，专门用来给子项目继承或者子项目可以单独直接依赖该项目的某个小功能模块来使用。


为了提高项目的开发效率，降低项目的维护成本，建议直接继承使用该基础设施项目，避免重复造轮子。`该项目后续也会继续迭代，来完善该基础功能架构。`

# 2 核心组件版本号
**核心组件版本号如下：**

| 组件名称 | 版本号 |
|:---:|:---:|
| Spring Version | 5.2.15.RELEASE |
| Spring Boot Version | 2.3.12.RELEASE |
| Spring Cloud Version | Hoxton.SR12 |
| Spring Cloud Openfeign | 2.2.9.RELEASE |
| Spring Cloud Alibaba Version | 2.2.9.RELEASE |
| Nacos Version | 2.1.1 |
| Seata Version | 1.5.2 |
| Spring Data Redis Version | 2.4.13 |
| Jedis Version | 3.3.0 |
| Redisson Version | 3.16.1 |
| MyBatis-Plus Version | 3.4.2 |
| HikariCP Version | 3.4.5 |
| MySQL Connector Version | 8.0.22 |
| Xxl-Job | 2.2.0 |
| JDK | 1.8 |


# 3 该微服务架构提供那些组件功能

**该项目主要包括以下功能模块：**
* `统一管理项目依赖，核心依赖的版本控制`
* `缓存管理以及分布式锁的处理`
* `分布式id功能`
* `预警通知功能`
* `异常管理`
* `多语言国际化功能`
* `限流Api管理`
* `Mock Server管理`
* `消息中间件MQ管理`
* `操作日志管理`
* `轻量级流程管理`
* `定时任务管理`
* `分布式事务管理`
* `项目安全管理`
* `Swagger-Ui管理`
* `工具类管理`
* `网关服务管理`

# 4 待完善的功能
* 集成分库分表功能
* 集成ELK管理模块

  如何快速的定位跟踪问题？那么，`SkyWalking` + `ELK` + `Sentry` + `钉钉预警`，在做一些核心数据的统计报表，对于一般的项目开发，应该足够用了


# 5 基础项目下载地址

* **GitHub下载地址：** [smilehappiness-architecture](https://github.com/smilehappinessli/smilehappiness-architecture.git)
* **Gitee下载地址：** [smilehappiness-architecture](https://gitee.com/smilehappiness/smilehappiness-architecture)


# 6 感谢参与维护此框架的小伙伴
## 6.1 2021年9~10月，完成了Spring Cloud微服务基础架构的技术选型，项目依赖的管理，以及核心功能模块的功能开发 @lijunwei
* `统一管理项目依赖，核心依赖的版本控制`
* `缓存管理以及分布式锁的处理`
* `预警通知功能`
* `异常管理`
* `限流Api管理`
* `Mock Server管理`
* `操作日志管理`
* `定时任务管理`
* `Swagger-Ui管理`
* `工具类管理`

## 6.2 2021年11月，完成了消息中间件以及网关服务的集成 @lizihao
* `消息中间件MQ管理`
* `网关服务管理`

## 6.3 2021年12月，完成了分布式id和国际化功能 @nibaoshan
* `多语言国际化功能`
* `分布式id功能`

## 6.4 2022年1月，集成了轻量级流程管理  @lijunwei
* `轻量级流程管理`

## 6.5 2021年10月~2023年2月，优化升级  @lijunwei
项目稳定运行了一年多，进行了相关优化，相关版本升级...

## 6.6 2023年3月，进行了安全升级以及集成了阿里的分布式事务  @lijunwei
* `Seata分布式事务管理`
* `项目安全管理` （非对称加密 加签验签 && 数据验证）

## 6.7 2023.3月，对阿里钉钉预警进行了api限流优化 @guoziran
* `钉钉预警Api限流优化`

