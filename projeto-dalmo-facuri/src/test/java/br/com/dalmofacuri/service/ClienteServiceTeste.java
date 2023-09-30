package br.com.dalmofacuri.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.dalmofacuri.builder.ClienteDTOBuilder;
import br.com.dalmofacuri.dto.ClienteDTO;
import br.com.dalmofacuri.exception.ClienteNotFoundException;
import br.com.dalmofacuri.mapper.ClienteMapper;
import br.com.dalmofacuri.model.Cliente;
import br.com.dalmofacuri.repository.ClienteRepository;
import br.com.dalmofacuri.service.ClienteService;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTeste {
	 
	private final ClienteMapper clienteMapper = ClienteMapper.INSTANCE;
    
	@Mock //banco de dados fictício
	private ClienteRepository clienteRepository;
	
	@InjectMocks  //injetando o Mock do repository para o service
	private ClienteService clienteService;
	
	private ClienteDTOBuilder clienteDTOBuilder;
	
	@BeforeEach()
	void setUp() {
		clienteDTOBuilder = ClienteDTOBuilder.builder().build();
		
	}
	
	@Test
	void testClienteFindAllSucess() {
		ClienteDTO clienteFoundDTO = clienteDTOBuilder.buildClienteDTO();
		Cliente clienteFound = clienteMapper.toModel(clienteFoundDTO);
		when(clienteRepository.findAll()).thenReturn(Collections.singletonList(clienteFound));
		List<ClienteDTO>foundClienteDto = clienteService.findAll();
	}
	
	@Test
	void testClienteFindAllNotSucess() {
		when(clienteRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
		List<ClienteDTO>foundClienteDto = clienteService.findAll();
	}
	
	@Test
	void testGetClienteSucess() {
		ClienteDTO clienteFoundDTO  = clienteDTOBuilder.buildClienteDTO();
		Cliente clienteFound = clienteMapper.toModel(clienteFoundDTO);
		when(clienteRepository.findById(clienteFound.getId()))
		                      .thenReturn(Optional.of(clienteFound));
		
		ClienteDTO foundClienteDTO = clienteService.findById(clienteFoundDTO.getId());
	
	}
	
	@Test
	void testeCreateClienteSucess() throws Exception {
		ClienteDTO expectedClienteTocreatedDTO = clienteDTOBuilder.buildClienteDTO();
	    Cliente expectedClienteCreated = clienteMapper.toModel(expectedClienteTocreatedDTO); 
	    when(clienteRepository.save(expectedClienteCreated)).thenReturn(expectedClienteCreated);
	    ClienteDTO createdClienteDTO = clienteService.create(expectedClienteTocreatedDTO);
	    MatcherAssert.assertThat(createdClienteDTO, Matchers.equalTo(expectedClienteTocreatedDTO));
	
	}
	
	@Test
	void testeUpdateClienteSucess() {
		ClienteDTO expectedClienteToUpdatedDTO = clienteDTOBuilder.buildClienteDTO();
		Cliente expectedClienteUpdate = clienteMapper.toModel(expectedClienteToUpdatedDTO); 
	    when(clienteRepository.findById(expectedClienteToUpdatedDTO.getId())).thenReturn(Optional.of(expectedClienteUpdate));
	    when(clienteRepository.save(expectedClienteUpdate)).thenReturn(expectedClienteUpdate);
	    ClienteDTO updateMessage = clienteService.update(expectedClienteToUpdatedDTO.getId(), expectedClienteToUpdatedDTO);
	    MatcherAssert.assertThat(updateMessage, is(equalTo(expectedClienteToUpdatedDTO)));
	
	}
	
	@Test
	void testeUpdateClienteNotSucess() {
		ClienteDTO expectedClienteToUpdatedDTO = clienteDTOBuilder.buildClienteDTO();
		Cliente expectedClienteUpdate = clienteMapper.toModel(expectedClienteToUpdatedDTO); 
	    when(clienteRepository.findById(expectedClienteToUpdatedDTO.getId())).thenReturn(Optional.empty());
	    assertThrows(ClienteNotFoundException.class, () -> clienteService.update(expectedClienteToUpdatedDTO.getId(), expectedClienteToUpdatedDTO));
	   }
	
	@Test
	void testDeleteClienteSucess() {
		ClienteDTO clienteDeletedDTO = clienteDTOBuilder.buildClienteDTO();
		Cliente clienteDeleted = clienteMapper.toModel(clienteDeletedDTO);
		Long clienteId = clienteDeletedDTO.getId();
		doNothing().when(clienteRepository).deleteById(clienteDeletedDTO.getId());
		when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteDeleted));
		clienteService.delete(clienteId);
		verify(clienteRepository, times(1)).deleteById(clienteId);
	
	}
	
	@Test
	void testDeleteClienteNotSucess() {
		Long  invalidClienteId = 2L;
		when(clienteRepository.findById(invalidClienteId)).thenReturn(Optional.empty());
		assertThrows(ClienteNotFoundException.class, () -> clienteService.delete(invalidClienteId));
	}
	
	 
	 
		
	 
}
