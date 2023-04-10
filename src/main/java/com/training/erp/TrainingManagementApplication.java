package com.training.erp;

import com.training.erp.security.jwt.AuthTokenFilter;
import com.training.erp.util.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@EnableCaching
public class TrainingManagementApplication implements CommandLineRunner {

	@Autowired
	private FilesStorageService filesStorageService;

	public static void main(String[] args) {
		SpringApplication.run(TrainingManagementApplication.class, args);
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthTokenFilter authTokenFilter(){
		return new AuthTokenFilter();
	}


	@Override
	public void run(String... args) throws Exception {
		//filesStorageService.init();
	}
}
