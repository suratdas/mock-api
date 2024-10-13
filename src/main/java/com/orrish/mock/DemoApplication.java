package com.orrish.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("This program produces API response based on files response.json & status.txt. To change file, use http://localhost/setFile?fileName=myfile.json");
		SpringApplication.run(DemoApplication.class, args);
	}
}