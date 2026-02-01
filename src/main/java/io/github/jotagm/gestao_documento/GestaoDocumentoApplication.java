package io.github.jotagm.gestao_documento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestaoDocumentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoDocumentoApplication.class, args);
	}

}
