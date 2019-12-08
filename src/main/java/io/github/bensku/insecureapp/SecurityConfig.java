package io.github.bensku.insecureapp;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation") // Eclipse complained something, probably not important
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource db;
	
	@PostConstruct
	private void initDb() throws SQLException {
		try (Connection conn = db.getConnection()) {
            PreparedStatement query = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY,"
            		+ "name VARCHAR,"
            		+ "password VARCHAR"
                    + ")");
            query.executeUpdate();
		}
	}
	
	@Autowired
	public void initialize(AuthenticationManagerBuilder authManager) throws Exception {
		authManager.jdbcAuthentication()
		.dataSource(db)
		.usersByUsernameQuery("SELECT name, password, 'true' FROM users WHERE name=?")
		.authoritiesByUsernameQuery("SELECT name, 'ROLE_BASIC' FROM users WHERE name=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.mvcMatchers("/register").anonymous()
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").permitAll()
		.and()
		.logout();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
