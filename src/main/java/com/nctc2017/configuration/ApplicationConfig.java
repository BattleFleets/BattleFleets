package com.nctc2017.configuration;

import com.nctc2017.services.*;
import com.nctc2017.services.utils.BattleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.mail.Session;
import javax.sql.DataSource;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
                .addResourceLocations("/static/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
                /*.setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new GzipResourceResolver())
                .addResolver(new PathResourceResolver());*/
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

    @Bean (name = "mailSender")
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol("smtp");
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("boottle.of.rum@gmail.com");
        javaMailSender.setPassword("ux73ffhw");
        Properties prop = new Properties();
        prop.put("mail.smtp.ssl.enable", "true");
        Session session = Session.getInstance(prop);
        javaMailSender.setSession(session);

        return javaMailSender;
    }

    @Bean(name = "travelServicePrototype")
    @Scope("prototype")
    public TravelService travelServiceProt() {
        return new TravelService();
    }
    
    @Bean(name = "battleManagerPrototype")
    @Scope("prototype")
    public BattleManager battleManagerProt() {
        return new BattleManager();
    }

    @Bean(name = "moneyServiceSingleton")
    @Scope("singleton")
    public MoneyService moneyServiceProt() {
        return new MoneyService();
    }

    @Bean(name = "shipServicePrototype")
    @Scope("prototype")
    public ShipService shipServiceProt() {
        return new ShipService();
    }

    @Bean(name = "travelServiceSingleton")
    @Scope("singleton")
    public TravelService travelServiceSing() {
        return new TravelService();
    }


    @Bean(name = "shipRepairService")
    @Scope("singleton")
    public ShipRepairService shipRepairService() {
        return new ShipRepairService();
    }

    @Bean(name = "shipTradeService")
    @Scope("prototype")
    public ShipTradeService shipTradeServiceTest() {
        return new ShipTradeService();
    }
    
    @Bean(name = "scheduledExecutorService")
    @Scope("singleton")
    public ScheduledExecutorService scheduledExecutorService() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(16);
        return executor;
    }

}
