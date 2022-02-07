package com.example.springtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
public class SpringTransactionApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringTransactionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Create the database table:
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS beers(name VARCHAR(100))");

		//Insert a record:
		jdbcTemplate.execute("INSERT INTO beers VALUES ('Stella')");

		//Read records:
		List<Beer> beers = jdbcTemplate.query("SELECT * FROM beers",
				(resultSet, rowNum) -> new Beer(resultSet.getString("name")));

		//Print read records:
		beers.forEach(System.out::println);
	}

}
