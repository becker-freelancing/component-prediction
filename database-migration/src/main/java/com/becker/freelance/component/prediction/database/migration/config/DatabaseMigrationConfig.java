package com.becker.freelance.component.prediction.database.migration.config;

import com.becker.freelance.component.prediction.database.migration.flyway.FlyWayRunner;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseMigrationConfig {

    @Bean
    @ConditionalOnMissingBean(Flyway.class)
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
    }

    @Bean
    public CommandLineRunner flywayRunner(Flyway flyway) {
        return new FlyWayRunner(flyway);
    }
}
