package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimplePageApplication {

	private ContentFetcher contentFetcher;
	public static void main(String[] args) {
		SpringApplication.run(SimplePageApplication.class, args);
	}

}
