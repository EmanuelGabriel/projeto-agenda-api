package br.com.srsolution.agenda.api.v1.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.srsolution.agenda.api.dtos.request.ClienteModelInputRequest;
import br.com.srsolution.agenda.api.dtos.request.ClienteModelParcialRequest;
import br.com.srsolution.agenda.api.dtos.response.ClienteModelResponse;
import br.com.srsolution.agenda.api.modelmapper.ClienteModelMapper;
import br.com.srsolution.agenda.domain.service.cliente.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "agenda_oauth")
@Tag(name = "Clientes", description = "Recurso de clientes")
@RestController
@RequestMapping(path = "/v1/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;
	private final ClienteModelMapper modelMapper;

	@Operation(description = "Lista de clientes", summary = "Lista de clientes")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não há clientes registrados"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping
	public ResponseEntity<Page<ClienteModelResponse>> listar(
			@PageableDefault(page = 0, size = 5, direction = Direction.ASC) @RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {

		var pageCliente = this.clienteService.listarTodos(PageRequest.of(page, size, Sort.by("codigo")));
		var pageClienteModelResponse = pageCliente.map(obj -> new ClienteModelResponse(obj));
		return pageCliente != null ? ResponseEntity.ok(pageClienteModelResponse) : ResponseEntity.ok().build();
	}

	@Operation(description = "Adiciona cliente", summary = "Adiciona cliente")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente inserido com sucesso"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ClienteModelResponse> criar(@Valid @RequestBody ClienteModelInputRequest request) {
		var cliente = this.modelMapper.toDto(request);
		this.modelMapper.toModel(this.clienteService.salvar(cliente));
		URI location = getUri(cliente.getCodigo());
		return ResponseEntity.created(location).build();
	}

	@Operation(description = "Busca cliente por ID", summary = "Busca cliente por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado cliente com este código"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("{codigoCliente}")
	public ResponseEntity<ClienteModelResponse> buscarPorCodigo(@PathVariable Long codigoCliente) {
		var cliente = this.modelMapper.toModel(this.clienteService.buscarPorCodigo(codigoCliente));
		return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Busca cliente por CPF", summary = "Busca cliente por CPF")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado cliente com este CPF"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("{cpf}/cpf")
	public ResponseEntity<ClienteModelResponse> buscarPorCpf(@PathVariable String cpf) {
		var cliente = this.modelMapper.toModel(this.clienteService.buscarPorCpf(cpf));
		return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Clientes com status ativo", summary = "Clientes com status ativo")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado cliente com status de ativo"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("por-ativo")
	public ResponseEntity<List<ClienteModelResponse>> buscarPorAtivo() {
		var clientes = this.clienteService.findByAtivo();
		return clientes != null ? ResponseEntity.ok(clientes) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Remove um cliente por ID", summary = "Remove um cliente por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado um cliente com este código"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@DeleteMapping("{codigoCliente}")
	public ResponseEntity<Void> remover(@PathVariable Long codigoCliente) {
		this.clienteService.excluir(codigoCliente);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "Atualiza um cliente por ID", summary = "Atualiza um cliente por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Cliente atualizado"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não há cliente cadastrado com este código"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PutMapping("{codigoCliente}")
	public ResponseEntity<ClienteModelResponse> atualizar(@PathVariable Long codigoCliente,
			@Valid @RequestBody ClienteModelParcialRequest request) {
		var cliente = this.modelMapper.toDto(request);
		this.clienteService.atualizar(codigoCliente, cliente);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(description = "Ativa cliente por ID", summary = "Ativa cliente por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado cliente com este código"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PatchMapping("{codigoCliente}/ativo")
	public ResponseEntity<Void> ativarStatus(@PathVariable Long codigoCliente) {
		this.clienteService.ativarStatus(codigoCliente);
		return ResponseEntity.ok().build();
	}

	@Operation(description = "Exibe a quantidade de clientes registrados", summary = "Exibe a quantidade de clientes registrados")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado cliente com este código"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping(value = "quantidade-cliente")
	public ResponseEntity<Long> quantidadeClientes() {
		var quantidade = this.clienteService.quantidade();
		return ResponseEntity.ok(quantidade);
	}

	private URI getUri(Long codigo) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(codigo).toUri();
	}

}
