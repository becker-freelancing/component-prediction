package com.becker.freelance.component.prediction.database.migration.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateOutput;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

public class FlyWayRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FlyWayRunner.class);

    private final Flyway flyway;

    public FlyWayRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(String... args) throws Exception {
        MigrateResult migrateResult = flyway.migrate();
        List<MigrateOutput> failedMigrations = migrateResult.getFailedMigrations();
        if (!failedMigrations.isEmpty()) {
            logger.error("Migration failed for {} Changesets", failedMigrations.size());
            failedMigrations.forEach(migrateOutput -> logger.error("Changeset: {} {} {}", migrateOutput.version, migrateOutput.description, migrateOutput.filepath));
        }

    }
}
