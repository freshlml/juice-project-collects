package com.fresh.juice.spring.context.autowireAnnoTest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class TestBeanForType<T, E extends TestBean3> {

    //注入点类型

    /**主要执行逻辑*/
    //根据Clazz查询候选bean: ListableBeanFactory#getBeanNamesForType(...)
    //候选过滤: AutowireCandidateResolver#isAutowireCandidate(...)
    //   如果注入点有@Qualifier注解，根据@Qualifier注解的比较来过滤; 如果注入点是泛型，根据注入点和候选Bean's BeanDefinition比较泛型类型过滤; 判断候选Bean's BeanDefinition#isAutowireCandidate
    //多个候选Bean determine: DefaultListableBeanFactory#determineAutowireCandidate(...)
    // @Primary、priority、beanName's match

    //1、泛型
    @Autowired
    private TestBeanGeneric1<String> testBeanGenericString;  //根据TestBeanGeneric1.class查询候选bean, 根据ParameterizedType比较泛型类型
    @Autowired
    private TestBeanGeneric1<T> testBeanGeneric1ForInteger;  //根据TestBeanGeneric1.class查询候选bean, 根据ParameterizedType比较泛型类型
    //@Autowired
    //private T t;            //根据Object.class查询候选bean，根据TypeVariable{T}比较泛型类型
    @Autowired
    private E e;              //根据TestBean3.class查询候选bean，根据TypeVariable{E extends TestBean3}比较泛型类型


    //2、Optional
    //如果根据Optional中的泛型类型找不到bean，则是null，纵然required=true
    @Autowired()
    private Optional<TestBean2> optionalTestBean2;  //根据TestBean2.class查询候选bean
    @Autowired(required = true)
    private Optional<NotExistsClz> optionalNotExistsClz;  //根据NotExistsClz.class查询候选bean
    @Autowired()
    private Optional<TestBeanGeneric1<String>> testBeanGeneric1ForString; //根据TestBeanGeneric1.class查询候选bean，根据ParameterizedType比较泛型类型
    //Optional和@Value注解一起没什么意义: @Value要提供默认值，该默认值不能设置为null
    @Value("${notExistsInteger:-1}")
    private Optional<Integer> optionalNotExistsInteger; //属性不存在时,Optional{-1}
    @Value("${notExistsInteger:-1}")
    private Integer notExistsInteger;          //属性不存在时，为-1
    //Optional没有@Lazy逻辑

    private static class NotExistsClz{}


    //3、ObjectFactory 、 ObjectProvider or Jssr330
    //延迟获取bean的一种方式，运行时调用doResolveDependency解析依赖，和Optional的解析逻辑一致
    //没有@Lazy逻辑


    //4、注入时@Lazy注解
    //@Lazy注解的搜寻逻辑:
    // if 注入点(Field、Method's Parameter)搜到@Lazy注解:
    //    return true
    // else if 注入点是Method's Parameter:
    //         if Method上搜到@Lazy注解:
    //             return true
    // return false
    //如果注入点判断需要Lazy: 返回动态代理对象，运行时调用doResolveDependency得到被代理对象

    //5、multi类型: 数组，Collection、Map、Stream
    //相对于"主要执行逻辑"，这里: 根据Clazz查询候选bean; 候选过滤; 封装成multi; 如果multi查询不到(返回null)继续走"主要执行逻辑"

    //5.1、Stream
    //@Autowired  //不会触发Stream逻辑(这种写法，将根据Stream.class查询候选Bean)
    //private Stream<TestBean4> streamTestBean4;
    //在ObjectProvider#stream()/#orderedStream()方法触发Stream类型
    @Autowired
    private ObjectProvider<TestBean4> streamTestBean4;  //根据TestBean4.class查询候选Bean，封装成Stream<TestBean4>返回
    //streamTestBean4.stream()触发
    //支持Order：OrderComparator

    //5.2、数组: 根据componentType查询候选bean
    @Autowired
    private TestBean5[] testBean5s;    //componentType=TestBean5.class，转换成数组返回
    @Autowired
    private T[] ts;                    //componentType=Object.class，转换成数组返回
    @Autowired
    private TestBeanGeneric1<T>[] tts; //componentType=TestBeanGeneric1.class，转换成数组返回
    //结果排序：OrderComparator

    //5.3、Collection 并且必须是interface: 根据elementType查询候选bean
    @Autowired
    private List<TestBean5> testBean5List;           //elementType=TestBean5.class，转换成List返回
    @Autowired
    private Set<TestBean5> testBean5Set;             //elementType=TestBean5.class，转换成Set返回
    //Order: 1、spring查询出的多个beans是有顺序的
    //       2、如果是List，会执行排序：OrderComparator

    //@Autowired
    //private List<TestBean5[]> testBean5Lists;        //elementType=TestBean5[].class
    //@Autowired
    //private List<TestBeanGeneric1<T>> testBean5ListGeneric;  //elementType=TestBeanGeneric1.class
    //@Autowired
    //private List<TestBeanGeneric1<T>[]> testBean5ListGenerics; //elementType=TestBeanGeneric1[].class

    //5.4、Map,key必须是String类型, 根据value.class查询候选bean
    @Autowired
    private Map<String, TestBean5> map;   //TestBean5.class


    //6、注入时的@Qualifier注解
    //候选过滤时，如果注入点有@Qualifier注解，根据@Qualifier注解的比较来过滤
    @Autowired
    @Qualifier("bean6One")
    private TestBean6 testBean6_11;
    @Autowired
    @Qualifier("bean6Two")
    private TestBean6 testBean6_22;
    //@Qualifier的解析支持元注解
    //搜索方式: 注入点(Field、Method Parameter)搜索，如果搜索不到并且是Method，在Method上搜索

}
