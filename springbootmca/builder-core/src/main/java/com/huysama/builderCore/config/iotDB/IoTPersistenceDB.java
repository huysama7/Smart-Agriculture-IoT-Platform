package com.huysama.builderCore.config.iotDB;

import com.huysama.builderCore.config.HamaPersistenceConfgiDB;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.huysama.builderCore.repositories.iot",
        entityManagerFactoryRef = "IoTDBEntityManager",
        transactionManagerRef = "IoTDBTransactionManager"
)
public class IoTPersistenceDB {

    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.jdbcUrl}")
    String jdbc_url;
    @Value("${spring.datasource.driver-class-name}")
    String driver;
    @Value("${spring.custom-datasource.minimum-idle}")
    Integer minimumidle;
    @Value("${spring.custom-datasource.maximum-pool-size}")
    Integer maximum_pool_size;
    @Value("${spring.datasource.entity-path}")
    String entity_path;

    @Bean(name = "IotHamaPersistenceConfgiDB")
    public HamaPersistenceConfgiDB iotHamaPersistenceConfgiDB() {
        return HamaPersistenceConfgiDB.builder()
                .username(this.username)
                .password(this.password)
                .jdbc_url(this.jdbc_url)
                .driver(this.driver)
                .entityPath(this.entity_path)
                .minimumIdle(this.minimumidle)
                .maximumPoolSize(this.maximum_pool_size)
                .build();
    }

    @Primary
    @Bean(name = "IoTDBDataSource")
    public DataSource dataSource(@Qualifier("IotHamaPersistenceConfgiDB") HamaPersistenceConfgiDB hamaPersistenceConfgiDB) {
        return hamaPersistenceConfgiDB.dataSource();
    }
    @Primary
    @Bean(name = "IoTDBEntityManager")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(@Qualifier("IotHamaPersistenceConfgiDB") HamaPersistenceConfgiDB hamaConfig,
                                                                                         EntityManagerFactoryBuilder builder,
                                                                                         @Qualifier("IoTDBDataSource") DataSource dataSource) {
        return hamaConfig.entityManagerFactory(builder, dataSource);
    }
    @Primary
    @Bean(name = "IoTDBTransactionManager")
    public PlatformTransactionManager customTransactionManager(@Qualifier("IoTDBEntityManager")
                                                                   EntityManagerFactory customEntityManagerFactory) {
        return new JpaTransactionManager(customEntityManagerFactory);
    }

}
