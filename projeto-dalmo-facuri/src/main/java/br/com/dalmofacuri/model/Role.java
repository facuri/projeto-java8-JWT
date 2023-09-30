package br.com.dalmofacuri.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1 )
public class Role implements GrantedAuthority {
	
	 private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role" )
	private Long id;
	private String nomeRole; //Papel, exemplo ROLE_SECRETARIO, ROLE_GERENTE...é o controle de acesso
	
	@Override
	public String getAuthority() { //Retorna o nome no papel, acesso ou autorização exemplo ROLE_GERENTE
		 
		return this.nomeRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRole() {
		return nomeRole;
	}

	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}
	
	

}
