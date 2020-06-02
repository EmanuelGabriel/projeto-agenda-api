package br.com.srsolution.agenda.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.domain.service.contato.ContatoService;
import br.com.srsolution.agenda.dtos.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recurso de Contatos", description = "Endpoints de contatos")
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/v1/contatos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ContatoController {

	private final ContatoService contatoService;

	@Operation(description = "Insere um contato", summary = "Cria um novo contato")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Contato inserido com sucesso."),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ContatoDTO> criar(@Valid @RequestBody ContatoDTO contatoDTO) {

		Contato contato = ContatoDTO.mapToEntidade(contatoDTO);
		ContatoDTO.mapToDto(this.contatoService.salvar(contato));
		URI location = getUri(contato.getCodigo());
		return ResponseEntity.created(location).build();

	}

	@GetMapping
	public ResponseEntity<List<ContatoDTO>> listar(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {
		List<ContatoDTO> contatos = this.contatoService.lista(PageRequest.of(page, size, Sort.by("nome")));
		return ResponseEntity.ok(contatos);
	}

	@GetMapping("{codigo}")
	public ResponseEntity<ContatoDTO> buscarPorCodigo(@PathVariable Long codigo) {
		ContatoDTO contato = this.contatoService.buscarPorCodigo(codigo);
		return contato != null ? ResponseEntity.ok(contato) : ResponseEntity.notFound().build();
	}

	@GetMapping("por-nome")
	public ResponseEntity<List<ContatoDTO>> buscarPorNome(String nome) {
		List<ContatoDTO> contatoPorNome = this.contatoService.buscarPorNome(nome);
		return contatoPorNome != null ? ResponseEntity.ok(contatoPorNome) : ResponseEntity.notFound().build();
	}

	@PatchMapping("{codigo}/favorito")
	public ResponseEntity<Void> favoritar(@PathVariable Long codigo) {
		this.contatoService.favoritar(codigo);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{codigo}")
	public ResponseEntity<Void> remover(@PathVariable Long codigo) {
		this.contatoService.excluir(codigo);
		return ResponseEntity.noContent().build();
	}

	private URI getUri(Long codigo) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(codigo).toUri();
	}

}
