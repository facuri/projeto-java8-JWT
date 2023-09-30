package br.com.dalmofacuri.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.com.dalmofacuri.builder.ClienteDTOBuilder;
import br.com.dalmofacuri.controller.ClienteController;
import br.com.dalmofacuri.dto.ClienteDTO;
import br.com.dalmofacuri.service.ClienteService;
import br.com.dalmofacuri.utils.JsonConversionUtils;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTeste {
	 
	private final static String CLIENTE_API_URL_PATH = "/clientes";
	
	private MockMvc mockMvc;
	
	@Mock
	ClienteService clienteService;
	
	@InjectMocks
	ClienteController clienteController;
	
	private ClienteDTOBuilder clienteDTOBuilder;
	
	@BeforeEach
	void setUp() {
		clienteDTOBuilder = ClienteDTOBuilder.builder().build();
		mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
				                 .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				                 .setViewResolvers((s, locale)-> new MappingJackson2JsonView())
				                 .build();
	
	}
	
	@Test
	void clienteFindAllOkStatus() throws Exception {
		ClienteDTO clienteDto = clienteDTOBuilder.buildClienteDTO();
		Mockito.when(clienteService.findAll()).thenReturn(Collections.singletonList(clienteDto));
		mockMvc.perform(MockMvcRequestBuilders.get(CLIENTE_API_URL_PATH)
				                             .contentType(MediaType.APPLICATION_JSON))
		                                     .andExpect(status().isOk())
		                                     .andExpect(jsonPath("$[0].id", Matchers.is(clienteDto.getId().intValue())))
		                                     .andExpect(jsonPath("$[0].nome", Matchers.is(clienteDto.getNome())))
		                                     .andExpect(jsonPath("$[0].email", Matchers.is(clienteDto.getEmail())))
		                                     .andExpect(jsonPath("$[0].telefone", Matchers.is(clienteDto.getTelefone())));
	
	}
	
	@Test
	void clienteFindByIdOkStatus() throws Exception {
		ClienteDTO clienteDto = clienteDTOBuilder.buildClienteDTO();
		Long  clienteDtoId = clienteDto.getId();
		Mockito.when(clienteService.findById(clienteDtoId)).thenReturn(clienteDto);
		mockMvc.perform(MockMvcRequestBuilders.get(CLIENTE_API_URL_PATH + "/" + clienteDtoId)
				                              .contentType(MediaType.APPLICATION_JSON))
		                                      .andExpect(status().isOk())
		                                      .andExpect(jsonPath("$.id", Matchers.is(clienteDto.getId().intValue())))
                                              .andExpect(jsonPath("$.nome", Matchers.is(clienteDto.getNome())))
                                              .andExpect(jsonPath("$.email", Matchers.is(clienteDto.getEmail())))
                                              .andExpect(jsonPath("$.telefone", Matchers.is(clienteDto.getTelefone())));
	
	}
	
	@Test
	void clienteCreateSucess() throws Exception {
		ClienteDTO clienteCreateDto = clienteDTOBuilder.buildClienteDTO();
		Mockito.when(clienteService.create(clienteCreateDto)).thenReturn(clienteCreateDto); //Mockando a classe service
		mockMvc.perform(MockMvcRequestBuilders.post(CLIENTE_API_URL_PATH)
				                              .contentType(MediaType.APPLICATION_JSON)
				                               .content(JsonConversionUtils.asJsonString(clienteCreateDto))) //converte de json para String
				                              .andExpect(status().isCreated())
				                              .andExpect(jsonPath("$.id", Matchers.is(clienteCreateDto.getId().intValue())))
                                              .andExpect(jsonPath("$.nome", Matchers.is(clienteCreateDto.getNome())))
                                              .andExpect(jsonPath("$.email", Matchers.is(clienteCreateDto.getEmail())))
                                              .andExpect(jsonPath("$.telefone", Matchers.is(clienteCreateDto.getTelefone())));
	 
	
	}
	
	@Test
	void clienteUpdateSucess() throws Exception {
		ClienteDTO clienteUpdateDto = clienteDTOBuilder.buildClienteDTO();
		Mockito.when(clienteService.update(clienteUpdateDto.getId(), clienteUpdateDto)).thenReturn(clienteUpdateDto);  
		mockMvc.perform(MockMvcRequestBuilders.put(CLIENTE_API_URL_PATH + "/" + clienteUpdateDto.getId())
				                              .contentType(MediaType.APPLICATION_JSON)
				                               .content(JsonConversionUtils.asJsonString(clienteUpdateDto)))  
				                              .andExpect(status().isOk())
				                              .andExpect(jsonPath("$.id", Matchers.is(clienteUpdateDto.getId().intValue())))
                                              .andExpect(jsonPath("$.nome", Matchers.is(clienteUpdateDto.getNome())))
                                              .andExpect(jsonPath("$.email", Matchers.is(clienteUpdateDto.getEmail())))
                                              .andExpect(jsonPath("$.telefone", Matchers.is(clienteUpdateDto.getTelefone())));
	 
	
	}
	
	@Test
	void clienteDeleteteSucess() throws Exception {
		ClienteDTO clienteToDeleteDTO = clienteDTOBuilder.buildClienteDTO();
		Long clienteIdToDelete = clienteToDeleteDTO.getId();
		doNothing().when(clienteService).delete(clienteIdToDelete);
		mockMvc.perform(delete(CLIENTE_API_URL_PATH + "/" + clienteIdToDelete)
				.contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent());
		
	}
	
 
}
