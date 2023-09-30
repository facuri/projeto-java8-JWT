package br.com.dalmofacuri.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.dalmofacuri.dto.AlexDTO;
import br.com.dalmofacuri.dto.ClienteDTO;
import br.com.dalmofacuri.model.Cliente;
import br.com.dalmofacuri.repository.ClienteRepository;
import br.com.dalmofacuri.service.ClienteService;
import io.swagger.annotations.Api;

@CrossOrigin(origins = "*") // liberar para todos
@RestController
@RequestMapping("/clientes")
@Api(value = "API REST Clientes")
public class ClienteController implements ClienteControllerDocs {

	private ClienteService clienteService;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Remove cache não utilizao por um longo período
	@CachePut("cacheusuarios") // Mudança no banco de dados e colocar no cachê
	public List<ClienteDTO> findAll() {
		return clienteService.findAll();
	}

	@GetMapping("/{id}")
	public ClienteDTO findById(@PathVariable Long id) {
		return clienteService.findById(id);

	}

	@PostMapping(value = "/", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDTO create(@Valid @RequestBody ClienteDTO clienteDTO) throws Exception {

		return clienteService.create(clienteDTO);

	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ClienteDTO update(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
		return clienteService.update(id, clienteDTO);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}

	@GetMapping("/usuarioPorNome/{nome}")
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Cliente>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException {

		List<Cliente> list = (List<Cliente>) clienteRepository.findUserByNome(nome);

		return new ResponseEntity<List<Cliente>>(list, HttpStatus.OK);

	}

}
