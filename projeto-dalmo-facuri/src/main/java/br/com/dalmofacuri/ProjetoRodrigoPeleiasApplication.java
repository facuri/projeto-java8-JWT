package br.com.dalmofacuri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
  
@SpringBootApplication
@ComponentScan(basePackages = {"br.*"})
public class ProjetoRodrigoPeleiasApplication  implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ProjetoRodrigoPeleiasApplication.class, args);
		
		   //System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	 
	//Mapeamento global que refletem todo o sistema
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/clientes/**")
		         .allowedMethods("*")//Libera para todos os EndPoints
		         .allowedOrigins("*");
		         
	}

}
