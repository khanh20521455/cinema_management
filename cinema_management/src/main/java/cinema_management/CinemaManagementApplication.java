package cinema_management;

import cinema_management.repository.MovieRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;

@SpringBootApplication
public class CinemaManagementApplication {
	public static void main(String[] args) {

		SpringApplication.run(CinemaManagementApplication.class, args);
	}

}
