package com.huysama.builderCore.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Log4j2
public class HamaPersistenceConfgiDB {
    private final String username;
    private final String password;
    private final String jdbc_url;
    private final String driver;
    private final String entityPath;
    private final String schema;
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
        String persistenceUnit = dbs.get(0);
        this.logInfo(persistenceUnit);
        return builder
                .dataSource(dataSource)
                .packages(this.entityPath)
                .persistenceUnit(persistenceUnit)
                .properties(Collections.singletonMap("hibernate.default_schema", this.schema))
                .build();
    }

    public void logInfo(String persistenceUnit) {
        System.out.println("###################--"+persistenceUnit+"--########################");
        System.out.println("Initializing HamaPersistenceConfgiDB with the following parameters:");
        System.out.println("Username: " + this.username);
        System.out.println("JDBC URL: " + this.jdbc_url);
        System.out.println("Driver: " + this.driver);
        System.out.println("Entity Path: " + this.entityPath);
        System.out.println("Schema: " + this.schema);
        System.out.println("Minimum Idle Connections: " + this.minimumIdle);
        System.out.println("Maximum Pool Size: " + this.maximumPoolSize);
        System.out.println("###########################################");
    }
}
