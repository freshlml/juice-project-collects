package com.project.normal.bean.bfs;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TempConfig {

    @Bean("temp")
    public Temp temp() {
        return new Temp();
    }

    public static class Temp{
    }

    public static class Temp1 implements InitializingBean {
        @Autowired
        private Temp temp;
        public Temp getTemp() {
            return temp;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("#################");
        }
    }

    public static void main(String argv[]) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.**.fl.bfs");

        AutowireCapableBeanFactory autowireCapableBeanFactory = ctx.getAutowireCapableBeanFactory();

        System.out.println(ctx.getBean(Temp.class));

        Temp1 t1 = autowireCapableBeanFactory.createBean(Temp1.class);

        System.out.println(t1);
        System.out.println(t1.getTemp());

        Temp1 t11 = new Temp1();
        autowireCapableBeanFactory.autowireBean(t11);
        System.out.println(t11);
        System.out.println(t11.getTemp());

    }

}
