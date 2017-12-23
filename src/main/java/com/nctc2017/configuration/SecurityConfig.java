package com.nctc2017.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		/*auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery(PersonPass)
        .authoritiesByUsernameQuery(PersonRole);*/
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                // указываем правила запросов
                // по которым будет определятся доступ к ресурсам и остальным данным
                .authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/**").access("hasRole('ROLE_USER')")
                .and();

        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // указываем URL при неудачном логине
                .failureUrl("/login?error")
                .defaultSuccessUrl("/city", true)
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("username")
                .passwordParameter("password")
                // устанавливаем success handler для маппинга админа/юзера
                //.successHandler(new UrlAuthenticationSuccessHandler(userDAO))
                // даем доступ к форме логина всем
                .permitAll();


        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutUrl("/logout")
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                // делаем не валидной текущую сессию
                .invalidateHttpSession(true);

        http.sessionManagement().maximumSessions(1);
    }
}
