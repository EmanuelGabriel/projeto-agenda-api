package br.com.srsolution.agenda.api.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.srsolution.agenda.api.modelmapper.ClienteModelMapper;
import br.com.srsolution.agenda.domain.model.Cliente;
import br.com.srsolution.agenda.domain.service.cliente.ClienteService;
import br.com.srsolution.agenda.dtos.ClienteDTO;
import br.com.srsolution.agenda.dtos.ClienteInputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recurso de Clientes", description = "Endpoints de clientes")
@RestController
@RequestMapping(value = "/v1/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;
	private final ClienteModelMapper modelMapper;

	@Operation(description = "Exibe uma lista de clientes com paginação", summary = "Exibe uma lista de clientes com paginação")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista de clientes exibida com sucesso."),
			@ApiResponse(responseCode = "404", description = "Não há clientes registrados"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping
	public ResponseEntity<Page<Cliente>> listar(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {
		Page<Cliente> clientes = this.clienteService.listarTodos(PageRequest.of(page, size, Sort.by("codigo")));
		return clientes != null ? ResponseEntity.ok(clientes) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Insere um cliente.", summary = "Cria um novo cliente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente inserido com sucesso."),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto.") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteInputDTO clienteInputDTO) {
		Cliente cliente = this.modelMapper.toDto(clienteInputDTO);
		this.clienteService.salvar(cliente);
		URI location = getUri(cliente.getCodigo());
		return ResponseEntity.created(location).build();
	}

	private URI getUri(Long codigo) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(codigo).toUri();
	}

}
