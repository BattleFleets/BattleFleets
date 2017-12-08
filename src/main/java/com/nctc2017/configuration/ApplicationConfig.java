package com.nctc2017.configuration;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.impl.CannonDaoImpl;
 
@Configuration
@ComponentScan(basePackages = "com.nctc2017")
@PropertySource(value = { "classpath:application_external_db.properties" })
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
    @Bean
    public CannonDao cannonDao(){
    	return new CannonDaoImpl();
    }
}
