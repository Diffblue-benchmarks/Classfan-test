package com.baison.e3plus.basebiz.order.service.configuration;

import com.alibaba.druid.pool.DruidDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@EnableConfigurationProperties({OrderRDSDataSourceProperties.class, OrderRDSMybatisProperties.class})
@MapperScan(basePackages = "com.baison.e3plus.basebiz.order.service.dao.mapper.rds", sqlSessionFactoryRef = "rdsSqlSessionFactory")
public class OrderRDSConfiguration implements TransactionManagementConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(OrderRDSConfiguration.class);

    @Autowired
    private OrderRDSDataSourceProperties rdsDataSourceProperties;

    @Autowired
    private OrderRDSMybatisProperties rdsMybatisProperties;

    private PlatformTransactionManager rdsTxManager;

    @Bean(name = "rdsDataSource")
    @Primary
    public DataSource rdsDataSource() throws SQLException {

        try(DruidDataSource dataSource = new DruidDataSource()) {

            dataSource.setName(rdsDataSourceProperties.getName());
            dataSource.setUrl(rdsDataSourceProperties.getUrl());
            dataSource.setUsername(rdsDataSourceProperties.getUsername());
            dataSource.setPassword(rdsDataSourceProperties.getPassword());
            dataSource.setDriverClassName(rdsDataSourceProperties.getDriverClassName());
            dataSource.setFilters(rdsDataSourceProperties.getFilters());
            dataSource.setMaxActive(rdsDataSourceProperties.getMaxActive());
            dataSource.setInitialSize(rdsDataSourceProperties.getInitialSize());
            dataSource.setMinIdle(rdsDataSourceProperties.getMinIdle());
            dataSource.setMaxWait(rdsDataSourceProperties.getMaxWait());
            dataSource.setTimeBetweenEvictionRunsMillis(rdsDataSourceProperties.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(rdsDataSourceProperties.getMinEvictableIdleTimeMillis());
            dataSource.setValidationQuery(rdsDataSourceProperties.getValidationQuery());
            dataSource.setTestWhileIdle(rdsDataSourceProperties.isTestWhileIdle());
            dataSource.setTestOnBorrow(rdsDataSourceProperties.isTestOnBorrow());
            dataSource.setTestOnReturn(rdsDataSourceProperties.isTestOnReturn());
            dataSource.setPoolPreparedStatements(rdsDataSourceProperties.isPoolPreparedStatements());
            dataSource.setMaxOpenPreparedStatements(rdsDataSourceProperties.getMaxOpenPreparedStatements());
            return dataSource;
        }
    }

    @Bean(name = "rdsJdbcTemplate")
    @Primary
    public JdbcTemplate rdsJdbcTemplate(@Qualifier("rdsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

//    @Bean(name = "rdsDataProvider")
//    @Primary
//    public DataProvider rdsDataProvider() {
//        return new DataProvider(JdbcTemplateChooser.RDS, false);
//    }

    @Bean(name = "rdsTxManager")
    @Primary
    public PlatformTransactionManager txManager(@Qualifier("rdsDataSource") DataSource dataSource) {

        DataSourceTransactionManager drdsTxManager = new DataSourceTransactionManager(dataSource);

        rdsTxManager = drdsTxManager;

        return rdsTxManager;
    }

    @Bean(name = "rdsSqlSessionFactory")
    public SqlSessionFactory rdsSqlSessionFactory(@Qualifier("rdsDataSource") DataSource dataSource) throws Exception {

        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(rdsMybatisProperties.getConfigLocation()));

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperXml;
        try {
            mapperXml = resolver.getResources(rdsMybatisProperties.getMapperLocations());
            sqlSessionFactoryBean.setMapperLocations(mapperXml);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        sqlSessionFactoryBean.setTypeAliasesPackage(rdsMybatisProperties.getTypeAliasesPackage());

        return sqlSessionFactoryBean.getObject();
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {

        return rdsTxManager;
    }

    @Bean(name = "rdsTxAdvice")
    public TransactionInterceptor getAdvisor() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("query*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("find*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("create*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("remove*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("modify*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("enable*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("disable*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("retreat*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("submit*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("term*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("check*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("complete*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("send*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("audit*", "PROPAGATION_REQUIRED,-Exception");
        PlatformTransactionManager rdsTxManager = this.annotationDrivenTransactionManager();
        TransactionInterceptor tsi = new TransactionInterceptor(rdsTxManager, properties);
        return tsi;

    }

    @Bean
    public BeanNameAutoProxyCreator rdsTxProxy() {
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setInterceptorNames("rdsTxAdvice");
        creator.setBeanNames("*Service", "*ServiceImpl");
        creator.setProxyTargetClass(true);
        return creator;
    }
}
