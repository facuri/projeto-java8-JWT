package br.com.dalmofacuri.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.dalmofacuri.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
     
	    @Query("select c from Cliente c where c.login = ?1")
	   Cliente findUserByLogin(String login);
	    
	    @Query("select c from Cliente c where c.nome like %?1%")
		  List<Cliente> findUserByNome(String nome);
	    
	    @Transactional
	    @Modifying
	    @Query(nativeQuery = true, value = "update cliente set token = ?1 where login = ?2")
	    void atualizaTokenUser(String token, String login);
	    
	    
	    
	
}
