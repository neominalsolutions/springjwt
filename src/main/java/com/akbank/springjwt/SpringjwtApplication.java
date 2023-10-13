package com.akbank.springjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.akbank.springjwt.config.JwtProperties;

@SpringBootApplication
// property dosyalarının uygulamaya tanıtılmasını sağlar.
@EnableConfigurationProperties(JwtProperties.class)
public class SpringjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringjwtApplication.class, args);
	}

}
