package br.com.srsolution.agenda.api.v1.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.srsolution.agenda.domain.service.cliente.RelatorioClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "agenda_oauth")
@Tag(name = "Relatório de Clientes", description = "Recurso de relatório de clientes")
@RestController
@RequestMapping(value = "/v1/relatorios", produces = MediaType.APPLICATION_JSON_VALUE)
public class RelatorioClienteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioClienteController.class);

	private static final String DATA_APPLICATION_PDF_BASE64 = "data:application/pdf;base64,";

	@Autowired
	private RelatorioClienteService relatorioClienteService;

	@Operation(description = "Exibe relatório de clientes", summary = "Exibe relatório de clientes")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "204", description = "No Content - sem conteúdo"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Relatório não encontrado"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping(value = "/clientes", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> downloadRelatorioCliente(HttpServletRequest request) throws Exception {

		LOGGER.info("GET /v1/relatorios/clientes - requisição para gerar relatório de cliente");

		byte[] relatorioCliente = this.relatorioClienteService.gerarRelatorio("relatorio-cliente",
				request.getServletContext());

		String base64PDF = DATA_APPLICATION_PDF_BASE64 + Base64.encodeBase64String(relatorioCliente);

		return base64PDF != null ? ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(base64PDF.getBytes())
				: ResponseEntity.noContent().build();
	}

	@Operation(description = "Exibe relatório de cliente por CPF", summary = "Exibe relatório de cliente por CPF")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "Requisição inválida (erro do cliente)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - não autorizado"),
			@ApiResponse(responseCode = "403", description = "Forbidden - Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "Relatório não encontrado"),
			@ApiResponse(responseCode = "406", description = "Recurso não possui representação que poderia ser aceita pelo consumidor"),
			@ApiResponse(responseCode = "500", description = "O servidor encontrou um erro não previsto") })
	@GetMapping(value = "/cliente-por-cpf", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> downloadRelatorioClienteCPF(@RequestParam("cpf") @RequestBody String cpf,
			HttpServletRequest request) throws Exception {

		LOGGER.info("GET /v1/relatorios/cliente-por-cpf - requisição para gerar relatório de cliente por CPF");

		Map<String, Object> parametroCPF = new HashMap<>();
		parametroCPF.put("CPF", cpf);

		byte[] relatorioCliente = this.relatorioClienteService.gerarRelatorio("relatorio-por-cpf-cliente", parametroCPF,
				request.getServletContext());

		String base64PDF = DATA_APPLICATION_PDF_BASE64 + Base64.encodeBase64String(relatorioCliente);

		return base64PDF != null ? ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(base64PDF.getBytes())
				: ResponseEntity.noContent().build();
	}

}
