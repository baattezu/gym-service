package org.saltaonelove.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.driver-class-name}") String driverClassName,
            @Value("${spring.datasource.url}") String connectionUrl,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto,
            @Value("${hibernate.dialect}") String dialect,
            @Value("${hibernate.show_sql}") String showSqlBool,
            @Value("${hibernate.validator.apply_to_ddl}") String applyToDdlBool
    ) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("org.saltaonelove.model");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        hibernateProperties.setProperty("hibernate.dialect", dialect);
        hibernateProperties.setProperty("hibernate.show_sql", showSqlBool);
        hibernateProperties.setProperty("hibernate.validator.apply_to_ddl", applyToDdlBool);
        factoryBean.setJpaProperties(hibernateProperties);

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
