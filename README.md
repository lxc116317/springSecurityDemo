<大概流程>
用户名登录--》校验--》登录相关结果集--》
（SecurityUtils.getCurrentUserId()主要的方法，权限下获取的人员id）
根据人员id获取关联下的菜单，权限不同展示的菜单不同


为什么需要匿名访问？
   除登录不需要带token访问以外，其他有些接口需要带token进行访问，需要进行匿名访问
  
  
实现匿名访问思路：
   1、自定义请求方法的注解
   2、通过applicationContext.getBean("requestMappingHandlerMapping")获取所有带有@RequestMapping注解的方法
   3、将所有带有@RequestMapping注解的方法存入容器中
   4、单独构建方法，此方法为了获取所有匿名的方法路径，然后通过springSecurity中的配置将获取的路径放行
   构建方法：
        1、 方法中参数传入容器
        2、 获取注解方法，将自定义注解中的公共参数传入，获取的返回值用来过滤不带注解的方法
        3、 通过枚举将方法名抽取出来，然后进行方法名判断
        4、 将所有的带有注解的方法名存入集合中
    