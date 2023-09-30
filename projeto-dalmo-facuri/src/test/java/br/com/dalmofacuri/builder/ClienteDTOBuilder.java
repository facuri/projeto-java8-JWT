package br.com.dalmofacuri.builder;

import br.com.dalmofacuri.dto.ClienteDTO;
import lombok.Builder;

@Builder
public class ClienteDTOBuilder {
	 
	@Builder.Default
	private final Long id = 1L;
	
	 
	private final String nome = "Dalmo Facuri";

	private final String email = "dalmo@terra.com.br";

	private final String telefone = "3651-3895";
	
	private final String login = "manoel";
	
	private final String senha = "123";
	
	//AQUI
	 private final String cep = "13990000";
	 
	 private final String logradouro ="R.prudente moraes";
	 
	 private final String complemento = "casa";
	 
	 private final String bairro = "centro";
	 
	 private final String localidade = "sdfss";
	 
	 private final String uf;
	
	 
	 
	 public ClienteDTO buildClienteDTO() {
		 return new ClienteDTO(id,
				               nome,
				              email,
				              telefone,
				              senha,
				              login,
				              cep,
				              logradouro,
				              complemento,
				              bairro,
				              localidade,
				              uf
				              
				              );
	 }
 
}
