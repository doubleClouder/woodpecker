package com.letv.woodpecker;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhusheng on 17/3/22.
 */
public class WoodpeckerServer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
    }
}
