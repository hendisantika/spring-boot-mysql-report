package com.hendisantika.springbootmysqlreport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-mysql-report
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 25/02/18
 * Time: 19.12
 * To change this template use File | Settings | File Templates.
 */

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("")
    public String index() {
        return "Hello World " + new Date();
    }

}