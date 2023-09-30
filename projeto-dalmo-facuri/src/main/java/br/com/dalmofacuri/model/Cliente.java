package br.com.dalmofacuri.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//import org.hibernate.annotations.ForeignKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
public class Cliente  implements UserDetails{

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	 @Column(unique = true)
	 private String login;
	 
	  
	  private String senha;

	 @NotBlank
	 @Size(max = 60)
	private String nome;

	 @NotBlank
	 @Email
	 @Size(max = 255)
	private String email;

	  @NotBlank
	 @Size(max = 20)
	private String telefone;
	  
	  @OneToMany(fetch = FetchType.EAGER)
	  @JoinTable(name = "cli_usuarios_role", uniqueConstraints = @UniqueConstraint(
			            columnNames = {"usuario_id", "role_id"}, name = "unique_role_user"), 
	  joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "cliente", unique = false, 
	  foreignKey = @javax.persistence.ForeignKey(name = "cliente_fk", value = ConstraintMode.CONSTRAINT)), 
	   inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", unique = false, updatable = false, 
	   foreignKey = @javax.persistence.ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT)) )
	   private List<Role> roles = new ArrayList<Role>(); //Os papeis ou acessos
	  
	  private String token = "";
	  
	  private String cep;
	  private String logradouro;
	  private String complemento;
	  private String bairro;
	  private String localidade;
	  private String uf;
	  
	 //São os acessos do Usuário ex: ROLE_ADMIN, ROLE_VISITANTE
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 
		return roles;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		 
		return this.senha;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		 
		return this.login;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		 
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		 
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		 
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		 
		return true;
	}
	  
	   

}
