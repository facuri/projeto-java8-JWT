package br.com.dalmofacuri.dto;

import java.io.Serializable;

import br.com.dalmofacuri.model.Cliente;

public class AlexDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nomeDTO;
	private String emailDTO;
	private String telefoneDTO;
	
	public String getNomeDTO() {
		return nomeDTO;
	}
	public void setNomeDTO(String nomeDTO) {
		this.nomeDTO = nomeDTO;
	}
	public String getEmailDTO() {
		return emailDTO;
	}
	public void setEmailDTO(String emailDTO) {
		this.emailDTO = emailDTO;
	}
	public String getTelefoneDTO() {
		return telefoneDTO;
	}
	public void setTelefoneDTO(String telefoneDTO) {
		this.telefoneDTO = telefoneDTO;
	}
	
	public AlexDTO(Cliente cliente) {
		
		this.setNomeDTO(cliente.getNome());
		this.setTelefoneDTO(cliente.getTelefone());
		this.setEmailDTO(cliente.getEmail());
		 
	}
	
	

}
