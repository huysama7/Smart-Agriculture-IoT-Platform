package com.huysama.builderCore.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class HamaPersistenceConfgiDB {
    private final String username;
    private final String password;
    private final String jdbc_url;
    private final String driver;
    private final String entityPath;
    private final int minimumIdle;
    private final int maximumPoolSize;

    public DataSource dataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create()
                .username(this.username)
                .password(this.password)
                .url(this.jdbc_url)
                .driverClassName(this.driver)
                .build();
        dataSource.setMinimumIdle(this.minimumIdle);
        dataSource.setMaximumPoolSize(this.maximumPoolSize);
        return dataSource;
    }

    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        List<String> dbs = Arrays.asList(this.entityPath.split("[.]"));
        Collections.reverse(dbs);
        return builder
                .dataSource(dataSource)
                .packages(this.entityPath)
                .persistenceUnit(dbs.get(0))
                .build();
    }


    public PlatformTransactionManager transactionManager(EntityManagerFactory customEntityManagerFactory) {
        return new JpaTransactionManager(customEntityManagerFactory);
    }
}
