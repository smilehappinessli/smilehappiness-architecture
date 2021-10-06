# 1 MockServer是什么
`MockServer`其实就是一个用来模拟`http（https）`请求响应结果数据的服务器。通过这个MockServer服务，我们可以极大地方便接口的调试。

# 2 为什么要使用MockServer
如今的业务系统模块越来越多，功能也越来越复杂。及时的与前端调试也迎来了一些小的挑战。

**假设有一个场景：** 
新项目刚开始启动时，这时候后台部分的接口都没有开发完成，这时候如果前端需要调试页面，该怎么调试呢？

傻傻的等着后台开发完成再进行调试？不可能的，这样你会影响项目正常上线。那么模拟数据就显得非常重要了，如何快速有效的模拟真实场景的数据？

**有两种方案：**
* 通常情况下，后台会把请求接口Api的结果先定义好，写死在action层，然后返回给前端，但是这种方案现在已经不怎么用了，效率太低
* 现在比较流行的方案，一般会`搭建一些server来进行mock`，这样`可以使得被开发功能的调试和测试功能能够正常进行下去`。而MockServer就可以有效的解决这个问题，这也是MockServer的出现的原因

**网上找了张图片，可以很好的说明使用MockServer前后的不同，如下图所示：**
**使用mock之前：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200816161405498.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NtaWxlaGFwcGluZXNz,size_16,color_FFFFFF,t_70#pic_center)
**使用mock之后：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200816161430479.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NtaWxlaGFwcGluZXNz,size_16,color_FFFFFF,t_70#pic_center)
使用了Mock Server之后，`前端可以不再依赖与后台的业务接口`，在后台接口未开发完成时，可以模拟一些业务数据，来进行前台页面的调试，`极大的节省了调试的成本`。

关于MockServer的使用，详细说明可以参考：[https://blog.csdn.net/smilehappiness/article/details/108037725](https://blog.csdn.net/smilehappiness/article/details/108037725)