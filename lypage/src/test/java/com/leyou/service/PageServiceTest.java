package com.leyou.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PageServiceTest {
@Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void create(){
        String msg ="李永波是个沙雕";
    amqpTemplate.convertAndSend("simple_queue",msg);
    }
}