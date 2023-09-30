package br.com.dalmofacuri.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.dalmofacuri.dto.ClienteDTO;
import br.com.dalmofacuri.model.Cliente;

@Mapper
public interface ClienteMapper {

	ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

	Cliente toModel(ClienteDTO clienteDTO);

	ClienteDTO toDTO(Cliente cliente);

}
