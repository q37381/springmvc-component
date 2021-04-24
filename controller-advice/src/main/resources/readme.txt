关于对controller返回对象的包装以及全局异常处理

1、springaop切面,对返回值进行包装或者全局异常处理
    - spring aop切面 像是直接将controller这个类代理了，所以返回值必须能够符合controller方法的返回值，不然就会出现类转换异常
    
    - 将controller返回值包装可以使用ResponseBodyAdvice接口来替换自定义aop切面，这样的话可以拦截所有controller而不需要配置切面的表达式，support方法里面也可以选择拦截或不拦截
    
    - aop切面 proceed 可以不捕获， 会当做controller抛的异常，后面的exceptionhandle会捕获到

2、全局异常处理
    - handlerExceptionResolver与@ControllerAdvice类似 处理全局异常，网传推荐使用注解，不推荐的原因可能是实现接口需要返回ModelAndView 不够灵活
    
    - HandlerExceptionResolver，SpringBoot框架默认有错误页面，如果这里返回null的话，抛了异常会到basicErrorController 错误页面 tomcat
        catalina里面有一个额errorPage会转发到/error（启动的时候设置进去的，onRefresh()->createWebServer()
        tomcat的factory 设置错误页面） 就到了basicErrorController 需要返回 new ModelAndView
        （ 如果在过滤器（Filter）中发生异常，或者调用的接口不存在，Spring会直接将Response的errorStatus状态设置成1，将http响应码设置为500或者404，Tomcat检测到errorStatus为1时，会将请求重现forward到/error接口；
          如果请求已经进入了Controller的处理方法，这时发生了异常，如果没有配置Spring的全局异常机制，那么请求还是会被forward到/error接口，如果配置了全局异常处理，Controller中的异常会被捕获；
          继承BasicErrorController就可以覆盖原有的错误处理方式。）
    - 使用aop切面方式拦截如果在处理的时候就往响应输出流写出去 调用了getWriter 要返回空 否则后续框架将会调用getWriter，而这个方法只能调用一次，所以会报异常
    
    - @ExceptionHandler修饰的方法的参数可以自行添加，框架会解析到响应的参数注入进去
    
    - @ExceptionHandler需要加上此注解，不然会当成视图，选择处理视图返回值的处理器来处理，导致没有视图所以使用默认当前视图导致循环报错，加上注解框架会 mavContainer.setRequestHandled(true)。 所以返回不会再由框架加上视图
    