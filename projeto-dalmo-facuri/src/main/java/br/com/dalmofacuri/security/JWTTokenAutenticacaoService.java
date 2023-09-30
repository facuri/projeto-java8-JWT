package br.com.dalmofacuri.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.dalmofacuri.ApplicationContexLoad;
import br.com.dalmofacuri.model.Cliente;
import br.com.dalmofacuri.repository.ClienteRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
//Gera o Token e valida o Token
public class JWTTokenAutenticacaoService {

	    //private static final long EXPIRATION_TIME = 172800000; //2 dias
	    private static final long EXPIRATION_TIME = 2073600000;//24 dias
	    
	    //Uma senha única para compor a autenticação e ajudar na segurança
	    private static final String SECRET = "SenhaExtremamenteSecreta";
	    
	    //Prefixo padrão de TOKEN
	    private static final String TOKEN_PREFIX = "Bearer";
	    
	    private static final String HEADER_STRING = "Authorization";
	    
	    //Gerando token de autenticação adicionando ao cabeçalho e resposta Http
	     public void addAuthentication(HttpServletResponse response, String username) throws IOException{
	    	 
	    	 //Montagem do Token 
	    	 String JWT = Jwts.builder()//Chama o gerador de token
	    			           .setSubject(username)//Adiciona o usuário
	    			           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//Tempo de expiração
	    			           .signWith(SignatureAlgorithm.HS512, SECRET).compact();//Compactação e algoritmos de geração de senha
	    	 
	    	 //Junta o token com o prefixo
	    	 String token = TOKEN_PREFIX + " " + JWT; //Bearer 243928slfolkfl3234ll
	    	 
	         //Adiciona o cabeçalho http
	    	 response.addHeader(HEADER_STRING, token); //Authorization: Bearer 243928slfolkfl3234ll
	    	 
	    	  //Liberando resposta para portas diferentes que usam a API ou caso clientes Web
	    	  liberacaoCors(response);
	    	 
	    	 //Escreve token como resposta no corpo http
	    	 response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	     }
	     
	     //Retorna o usuário validado com token ou caso não seja valido retorna null.
	     public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) { //request é requisição
	    	 
	    	  //Pega o Token enviado no cabeçalho http
	    	  String token = request.getHeader(HEADER_STRING);//consultando dentro do cabeçalho
	    	 
	     
	    	  if(token != null) {
	    		  
	    		  //Faz a validação do token do usuário na requisição 
	    		  String user = Jwts.parser().setSigningKey(SECRET)//Bearer 243928slfolkfl3234ll
	    				                       .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))//243928slfolkfl3234ll
	    				                       .getBody().getSubject();//João Silva
	    		  
	    		  if(user != null) {
	    			  
	    			    Cliente usuario = ApplicationContexLoad.getApplicationContext()
	    			    		                               .getBean(ClienteRepository.class).findUserByLogin(user);
	    			    
	    			     if(usuario != null) {
	    			    	 
	    			    	       return new UsernamePasswordAuthenticationToken(
	    			    			   usuario.getLogin(), 
	    			    			   usuario.getSenha(), 
	    			    			   usuario.getAuthorities());
	    			    	 }
	    			     } 
	    		    } 
	    	  
	    	   
	     //Liberando resposta para portas diferentes que usam a API ou caso clientes Web
	     liberacaoCors(response);
	    	  
	      return null; //Não autorizado
	    	   
	     }

		private void liberacaoCors(HttpServletResponse response) {
			 
			if (response.getHeader("Access-Control-Allow-Origin") == null) {
				response.addHeader("Access-Control-Allow-Origin", "*");
			}
			
			if (response.getHeader("Access-Control-Allow-Headers") == null) {
				response.addHeader("Access-Control-Allow-Headers", "*");
			}
			
			
			if (response.getHeader("Access-Control-Request-Headers") == null) {
				response.addHeader("Access-Control-Request-Headers", "*");
			}
			
			 if(response.getHeader("Access-Control-Allow-Methods") == null) {
				 response.addHeader("Access-Control-Allow-Methods", "*");
			 }
			
			 
			
			
			
		}
	 
}
