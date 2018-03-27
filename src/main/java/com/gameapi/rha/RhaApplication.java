package com.gameapi.rha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@SpringBootApplication
public class RhaApplication {

//  @Bean
//  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//    return new JdbcTemplate(dataSource);
//  }
  public static void main(String[] args) {
    SpringApplication.run(RhaApplication.class, args);
  }
}
