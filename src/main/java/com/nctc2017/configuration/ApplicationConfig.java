package com.nctc2017.configuration;

import com.nctc2017.services.TravelService;
import com.nctc2017.services.utils.BattleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Locale;


@Configuration
@ComponentScan(basePackages = "com.nctc2017")
@PropertySource(value = { "classpath:application_external_db.properties" })
@EnableTransactionManagement
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        Locale.setDefault(Locale.ENGLISH);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        final PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        return transactionManager;
    }
    @Bean(name = "travelServicePrototype")
    @Scope("prototype")
    public TravelService travelServiceProt() {
        return new TravelService();
    }

    @Bean(name = "travelServiceSingleton")
    @Scope("singleton")
    public TravelService travelServiceSing() {
        return new TravelService();
    }

    @Bean
    @Scope("singleton")
    public BattleManager battles() {
        return new BattleManager();
    }
}
