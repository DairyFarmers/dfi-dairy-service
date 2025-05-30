package com.example.dairyinventoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseInitializer {

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("functions/DropTablesOrFunctionsIfExists.sql"));
//        populator.addScript(new ClassPathResource("functions/FarmerFunctions.sql"));
        populator.addScript(new ClassPathResource("functions/PostInventoryDetails.sql"));
        populator.addScript(new ClassPathResource("functions/GetInventoryDetails.sql"));
        populator.addScript(new ClassPathResource("functions/B2bFunctions.sql"));
        populator.addScript(new ClassPathResource("functions/CreateRequiredDataToCheckSqlFunctions.sql"));
        populator.addScript(new ClassPathResource("functions/ChatMessage.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(true);

        return initializer;
    }

}
