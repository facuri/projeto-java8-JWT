package br.com.dalmofacuri.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import br.com.dalmofacuri.dto.AlexDTO;
import br.com.dalmofacuri.dto.ClienteDTO;
import br.com.dalmofacuri.exception.ClienteNotFoundException;
import br.com.dalmofacuri.mapper.ClienteMapper;
import br.com.dalmofacuri.model.Cliente;
import br.com.dalmofacuri.repository.ClienteRepository;
 

@Service
public class ClienteService implements UserDetailsService{
	
	private static final ClienteMapper clienteMapper = ClienteMapper.INSTANCE;
	
	private ClienteRepository clienteRepository;
	
	@Autowired
	public ClienteService(ClienteRepository clienteRepository) {
		 this.clienteRepository = clienteRepository;
	}
	
	public List<ClienteDTO> findAll(){
		return clienteRepository.findAll()
				                .stream()
				                //.map((n) -> clienteMapper.toDTO(n)) 
				                .map(clienteMapper::toDTO)//referencia
				                .collect(Collectors.toList());
	}
	
	public ClienteDTO findById(Long id) {
		return clienteRepository.findById(id)
			   //.map((n) -> clienteMapper.toDTO(n))
				 .map(clienteMapper::toDTO)
			   .orElseThrow(() -> new ClienteNotFoundException(id));
	}
	
	 
	 @Transactional
	public ClienteDTO create(ClienteDTO clienteDTO) throws Exception  {
		
		Cliente clienteToCreate = clienteMapper.toModel(clienteDTO);
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(clienteToCreate.getSenha());
		clienteToCreate.setSenha(senhaCriptografada);
		Cliente createCliente = clienteRepository.save(clienteToCreate);
		
		return clienteMapper.toDTO(createCliente) ;
	}
	 
	
	@Transactional
	public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
	    verifyExists(id);
		Cliente cli = new Cliente();
		cli.setId(id);
		clienteDTO.setId(cli.getId());
		Cliente clienteToCreate = clienteMapper.toModel(clienteDTO);
		
		 
		Cliente clienteTemporario = clienteRepository.findById(clienteDTO.getId()).get();
		
		 
		if(!clienteTemporario.getSenha().equals(clienteToCreate.getSenha())) {//senhas diferentes
			String senhaCriptografada = new BCryptPasswordEncoder().encode(clienteToCreate.getSenha());
	        clienteToCreate.setSenha(senhaCriptografada);
		}
		  
		Cliente createCliente = clienteRepository.save(clienteToCreate);
		
		return clienteMapper.toDTO(createCliente) ;
	}
	
	 @Transactional
	public void delete(Long id) {
		verifyExists(id);
		clienteRepository.deleteById(id);
	}
	
	private void verifyExists(Long id) {
		 clienteRepository.findById(id)
		                  .orElseThrow(() -> new ClienteNotFoundException(id));
	 
	  }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		   
		   //Consultar no banco o Cliente
		  Cliente cliente = clienteRepository.findUserByLogin(username);
		  
		  if(cliente == null) {
			  throw new UsernameNotFoundException("Usuário não foi encontrado!");
		  }
		return new User(cliente.getLogin(), 
				cliente.getPassword(), 
				cliente.getAuthorities());
	}
	
	
	 
	
	 

}
