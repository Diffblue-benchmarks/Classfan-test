package com.baison.e3plus.basebiz.order.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.rds")
public class OrderRDSDataSourceProperties extends OrderDruidDataSourceProperties{

}
