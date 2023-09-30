package br.com.dalmofacuri.controller;

import java.util.List;

import br.com.dalmofacuri.dto.ClienteDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("API REST Clientes")
public interface ClienteControllerDocs {

	@ApiOperation(value = "Lista todos os registros dos clientes")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorna todos os registros dos clientes"),

	})
	List<ClienteDTO> findAll();

	@ApiOperation(value = "Localizar cliente por operação de ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso cliente encontrado"),
			@ApiResponse(code = 404, message = "Código de cliente não encontrado"), })
	ClienteDTO findById(Long id);

	@ApiOperation(value = "Operação de criação do cliente")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Cliente criado com sucesso"),
			@ApiResponse(code = 400, message = "Faltam campos obrigatórios, valor de intervalo de campo errado ou cliente já cadastrado no sistema."), })
	ClienteDTO create(ClienteDTO clienteDTO) throws Exception;

	@ApiOperation(value = "Atualizar cliente por operação de ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso cliente atualizado"),
			@ApiResponse(code = 404, message = "Código de erro cliente não encontrado"), })
	ClienteDTO update(Long id, ClienteDTO clienteDTO);

	@ApiOperation(value = "Excluir cliente por operação de id")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Sucesso cliente deletado"),
			@ApiResponse(code = 404, message = "Código de erro cliente não encontrado"), })
	void delete(Long id);

}
