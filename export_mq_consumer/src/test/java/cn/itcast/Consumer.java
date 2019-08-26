package cn.itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:applicationContext-rabbitmq-consumer.xml");

        System.in.read();

    }
}
