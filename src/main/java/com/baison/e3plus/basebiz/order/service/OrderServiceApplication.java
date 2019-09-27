package com.baison.e3plus.basebiz.order.service;

import com.baison.e3plus.common.bscore.resource.ResourceLoaderManager;
import com.github.pagehelper.PageHelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import cn.com.pg.paas.monitor.EnableHealthPrometheusMvc;


@RefreshScope
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.baison.e3plus.basebiz.order","com.baison.e3plus.common"})
@EnableFeignClients(basePackages = {"com.baison.e3plus.basebiz.order.service.feignclient"})
@EnableAsync
@EnableEurekaClient
@EnableHealthPrometheusMvc
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        //MetadataFactory.getInstance();
        ResourceLoaderManager.load();
    }

    //配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");    //配置mysql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
