package com.fresh.juice.spring.context.autowireAnnoTest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class TestBeanForBasic {

    //将注解加在Field或者Method上才能被find metadate; Method的所有Parameter作为注入点
    //1、将@Value注解或者@Autowired注解放在Field上
    //   注入点是Field
    //必须是非static成员、基类中定义的同样可以、declared(所有public和非public的)

    //2、将@Value注解或者@Autowired注解放在Method上
    //   注意！注入点是Method的所有Parameter
    //必须是非static成员、基类中定义的同样可以、declared(所有public和非public的)

    @Autowired
    private TestBean2 testBean2;
    private TestBean3 testBean3;
    private TestBean4 testBean4;
    //Parameter上加@Autowired注解是没有任何影响的
    @Autowired
    public void setTestBean3(TestBean3 testBean3, TestBean4 testBean4) {
        this.testBean3 = testBean3;
        this.testBean4 = testBean4;
    }

    //note: @Value注解不写默认值是很危险的，因为一旦该属性不存在，以原样字符串返回，则类型转换时error
    @Value("${corePoolSize:100}")
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer blockingQueueSize;
    //当@Value用在Method上时，可以在Parameter也加上@Value注解
    //Parameter上的Value注解优先于Method上的Value注解
    @Value("${maxPoolSize:50}")
    public void setCorePoolSize(Integer maxPoolSize, @Value("${blockingQueueSize:1000}") Integer blockingQueueSize) {
        this.maxPoolSize = maxPoolSize;
        this.blockingQueueSize = blockingQueueSize;
    }
    @Value("${integerDefaultValue:-1}")  //默认值不能是null,null转换成Integer报错
    private Integer integerDefaultValue;  //-1
    @Value("${integerDefaultValue:}")  //默认值如果是null,null将转换成"null"
    private String stringDefaultValue;   //""
    //@Value注解在解析时包括直接定义的@Value注解和元注解上定义的@Value注解


    //如果Field上同时存在@Autowired和@Value，@Value注解的解析代码优先执行，这里是存在歧义的用法
    /*@Value("corePoolSize")
    @Autowired
    private Integer corePoolSize2;*/

    //如果Method上同时存在@Autowired和@Value，@Value注解的解析代码优先执行，这里是存在歧义的用法
    /*@Autowired
    @Value("${maxPoolSize}")
    public void setCorePoolSize(Integer maxPoolSize, Integer blockingQueueSize) {
        this.maxPoolSize = maxPoolSize;
        this.blockingQueueSize = blockingQueueSize;
    }*/

    //还算比较正常的用法，@Value优先
    @Autowired
    public void setCorePoolSize2(@Value("${maxPoolSize:50}") Integer maxPoolSize,
                                 @Value("${blockingQueueSize:1000}") Integer blockingQueueSize) {
        //this.maxPoolSize = maxPoolSize;
        //this.blockingQueueSize = blockingQueueSize;
    }



    //@Resource与@Autowired的比较
    //@Resource放在Field、Method，@Autowired可放在Parameter、Constructor
    //@Resource注解放在Method上时，必须要求Method只能有一个参数，并且如果name属性为空则其方法名称还必须是set开头
    //@Resource注解解析依赖的方法不同: name属性(默认为空"")、type属性(默认为Object.class)
    //  1、如果name属性不为空，支持string resolve，否则解析注入点的名称
    //  2、如果type属性不为Object.class，check注入点的类型匹配，否则获取注入点的Class类型
    //  3、一般情况下getBean(名称, 类型), 但也可以控制采用resolveDependency


}
