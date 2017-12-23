package com.nctc2017.configuration;

import java.util.Locale;

import javax.sql.DataSource;

import com.nctc2017.services.LevelUpService;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.ShipService;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.nctc2017")
@Import({ SecurityConfig.class })
@PropertySource(value = { "classpath:application_external_db.properties" })
@EnableTransactionManagement
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver
                = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        Locale.setDefault(Locale.ENGLISH);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean(name="jdbcTemplate")
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

    @Bean(name = "moneyServicePrototype")
    @Scope("prototype")
    public MoneyService moneyServiceProt() {
        return new MoneyService();
    }

    @Bean(name = "shipServicePrototype")
    @Scope("prototype")
    public ShipService shipServiceProt() {
        return new ShipService();
    }

    @Bean(name = "levelUpServicePrototype")
    @Scope("prototype")
    public LevelUpService levelUpServiceProt() {
        return new LevelUpService();
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
