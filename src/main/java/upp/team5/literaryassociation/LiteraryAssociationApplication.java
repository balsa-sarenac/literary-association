package upp.team5.literaryassociation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class LiteraryAssociationApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraryAssociationApplication.class, args);
	}

}
