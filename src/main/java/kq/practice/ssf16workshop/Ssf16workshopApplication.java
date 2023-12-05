package kq.practice.ssf16workshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ssf16workshopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Ssf16workshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Redis connected");
	}

}