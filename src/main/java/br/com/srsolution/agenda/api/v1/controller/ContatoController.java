package br.com.srsolution.agenda.api.v1.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import br.com.srsolution.agenda.api.dtos.ContatoDTO;
import br.com.srsolution.agenda.api.dtos.ContatoInputDTO;
import br.com.srsolution.agenda.api.modelmapper.ContatoModelMapper;
import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.domain.service.contato.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recurso de Contatos", description = "Endpoints de contato")
@RestController
@RequestMapping(value = "/v1/contatos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ContatoController {

	private final ContatoService contatoService;
	private final ContatoModelMapper modelMapper;

	@Operation(description = "Insere um contato.", summary = "Cria um novo contato.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Contato inserido com sucesso."),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto.") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ContatoDTO> criar(@Valid @RequestBody ContatoInputDTO contatoInputDTO) {

		Contato contato = this.modelMapper.toDto(contatoInputDTO);
		this.contatoService.salvar(contato);
		URI location = getUri(contato.getCodigo());
		return ResponseEntity.created(location).build();

	}

	@Operation(description = "Exibe uma lista de contatos com paginação", summary = "Exibe uma lista de contatos com paginação")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista de contatos exibida com sucesso."),
			@ApiResponse(responseCode = "404", description = "Não há contatos registrados"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping
	public ResponseEntity<Page<Contato>> listar(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {
		Page<Contato> contatos = this.contatoService.lista(PageRequest.of(page, size, Sort.by("codigo")));
		return contatos != null ? ResponseEntity.ok(contatos) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Busca um contato por seu código ou id", summary = "Busca um contato por seu código ou id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Contato encontrado por seu código"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado contato com este código"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("{codigo}")
	public ResponseEntity<Contato> buscarPorCodigo(@PathVariable Long codigo) {
		var contato = this.contatoService.findByCodigo(codigo);
		return contato != null ? ResponseEntity.ok(contato) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Busca um contato por seu nome", summary = "Busca um contato por seu nome")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Realiza a busca de contato por seu nome"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado contato com este sugerido"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("por-nome")
	public ResponseEntity<List<ContatoDTO>> buscarPorNome(String nome) {
		List<ContatoDTO> contatoPorNome = this.contatoService.buscarPorNome(nome);
		return contatoPorNome != null ? ResponseEntity.ok(contatoPorNome) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Busca um contato por seu telefone", summary = "Busca um contato por seu telefone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Realiza a busca de contato por seu telefone"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado contato com este número"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping("por-telefone")
	public ResponseEntity<List<ContatoDTO>> buscarPorTelefone(@RequestParam("telefone") String telefone) {
		List<ContatoDTO> contatoPorTelefone = this.contatoService.buscarPorTelefone(telefone);
		return contatoPorTelefone != null ? ResponseEntity.ok(contatoPorTelefone) : ResponseEntity.notFound().build();
	}

	@Operation(description = "Favorita um contato por seu código", summary = "Favorita um contato por seu código")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Contato favoritado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado contato com este código"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PatchMapping("{codigo}/favorito")
	public ResponseEntity<Void> favoritar(@PathVariable Long codigo) {
		this.contatoService.favoritar(codigo);
		return ResponseEntity.ok().build();
	}

	@Operation(description = "Faz a atualização de contato por seu código", summary = "Faz a atualização de contato por seu código")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Contato atualizado por seu código já existente"),
			@ApiResponse(responseCode = "404", description = "Não há contato cadastrado com este código"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PutMapping("{codigo}")
	public ResponseEntity<Void> atualizar(@PathVariable Long codigo, @Valid @RequestBody ContatoDTO contatoDTO) {

		Contato contato = this.modelMapper.toDto(contatoDTO);
		this.contatoService.atualizar(codigo, contato);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(description = "Remove um contato por seu código", summary = "Remove um contato por seu código")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Contato removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Não foi encontrado um contato com este código"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@DeleteMapping("{codigo}")
	public ResponseEntity<Void> remover(@PathVariable Long codigo) {
		this.contatoService.excluir(codigo);
		return ResponseEntity.noContent().build();
	}

	private URI getUri(Long codigo) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(codigo).toUri();
	}

}
