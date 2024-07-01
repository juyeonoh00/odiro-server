package odiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
//@EnableJpaAuditing
//@EnableWebSecurity
public class OdiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdiroApplication.class, args);
	}

}
