package com.baison.e3plus.basebiz.order;

import com.alibaba.druid.pool.DruidDataSource;
import com.baison.e3plus.basebiz.order.service.OrderServiceApplication;
import com.baison.e3plus.basebiz.order.service.configuration.XxlJobConfig;
import com.baison.e3plus.basebiz.order.service.h2.H2Utils;
import com.baison.e3plus.common.bscore.resource.ResourceLoaderManager;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.sql.DataSource;
import java.util.Objects;

@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class, RabbitAutoConfiguration.class})
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.baison.e3plus.basebiz.order.service"}
        , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE
        , classes = {XxlJobConfig.class, OrderServiceApplication.class}))
@EnableFeignClients(basePackages = {"com.baison.e3plus.basebiz.order.service.feignclient"})
public class TestApplication{

    @TestConfiguration
    public class InitConfiguration {
        @Autowired
        private DataSource dataSource;
        @Bean
        public ApplicationListener initData() {
            return new ApplicationListener<ContextRefreshedEvent>() {
                @Override public void onApplicationEvent(ContextRefreshedEvent event) {
                    if (Objects.equals(event.getApplicationContext().getClass().getSimpleName(),
                            GenericWebApplicationContext.class.getSimpleName())) {
                        //加载提示信息配置
                        //ResourceLoaderManager.load();
                        //初始化H2数据库表结构
                        H2Utils.initSchema(dataSource);
                    }
                }
            };
        }
    }

}
