package br.com.dalmofacuri.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import br.com.dalmofacuri.service.ClienteService;

//Mapeia URLS, endereços, autoriza ou bloqueia acesso a URLS
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ClienteService clienteService;
	
	//Configura as solicitações de acesso por Http
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 //Ativando a proteção contra usuários que não estão validados por TOKEN
	      http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	      
	      //Ativando a permissão para acesso a página inicial do sistema EX: sistema.com.br/index
	      .disable().authorizeRequests().antMatchers("/").permitAll()
	      .antMatchers("/index").permitAll()
	      
	       .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()//HTTPS são liberados(PUT, POST, DELETE)
	      
	        //URL de Logout - Redireciona após o user do sistema
	       .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
	       //Mapeia URL de Logout e invalida o usuário
	      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	      //Filtra requisições de login para a autenticação 
	      .and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
	    		  UsernamePasswordAuthenticationFilter.class)
	      //Filtra demais requisições para verificar a presença do JWT no Header HTTP
	      .addFilterBefore(new JWTApiAutenticacaoFilter(),UsernamePasswordAuthenticationFilter.class);
	      
	
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 
		//Service que irá consultar o usuário no banco de dados
		auth.userDetailsService(clienteService)
		     //Padrão de codificação de senha
		    .passwordEncoder(new BCryptPasswordEncoder());
	}

}
