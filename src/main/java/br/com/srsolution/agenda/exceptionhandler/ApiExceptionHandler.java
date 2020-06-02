package br.com.srsolution.agenda.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.srsolution.agenda.domain.exception.EntidadeNaoEncontradaException;
import br.com.srsolution.agenda.domain.exception.RegraNegocioException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	private static final String VALIDACAO_CAMPOS = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var campos = new ArrayList<Problema.Campo>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			campos.add(new Problema.Campo(nome, mensagem));
		}

		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(VALIDACAO_CAMPOS);
		problema.setDataHora(LocalDateTime.now());
		problema.setCampos(campos);

		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		var tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		var detalhe = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

		var problema = criarProblemaBuilder(status, tipoProblema, detalhe).mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(RegraNegocioException.class)
	public ResponseEntity<?> handleRegraNegocioException(RegraNegocioException ex, WebRequest request) {

		var status = HttpStatus.BAD_REQUEST;
		var tipoProblema = TipoProblema.ERRO_NEGOCIO;
		var detalheErro = ex.getMessage();

		var problema = criarProblemaBuilder(status, tipoProblema).mensagem(detalheErro).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> entidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {

		var status = HttpStatus.NOT_FOUND;
		var tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		var detalhe = ex.getMessage();

		var problema = criarProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var problemType = TipoProblema.PARAMETRO_INVALIDO;

		var detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		var problema = criarProblemaBuilder(status, problemType, detail).mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> dataIntegrityException(DataIntegrityViolationException ex, WebRequest request) {

		var status = HttpStatus.CONFLICT;
		var tipoProblema = TipoProblema.ERRO_INTEGRIDADE_DADOS;
		var detalhe = ex.getMessage();

		var problema = criarProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = ProblemaResponse.builder().timestamp(LocalDateTime.now()).titulo(status.getReasonPhrase())
					.status(status.value()).mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		} else if (body instanceof String) {
			body = ProblemaResponse.builder().timestamp(LocalDateTime.now()).titulo((String) body)
					.status(status.value()).mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private ProblemaResponse.ProblemaResponseBuilder criarProblemaBuilder(HttpStatus status,
			TipoProblema tipoProblema) {

		return ProblemaResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.tipo(tipoProblema.getUri()).titulo(tipoProblema.getTitulo());
	}

	private ProblemaResponse.ProblemaResponseBuilder criarProblemaBuilder(HttpStatus status, TipoProblema tipoProblema,
			String detalhe) {

		return ProblemaResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.tipo(tipoProblema.getUri()).titulo(tipoProblema.getTitulo()).detalhe(detalhe);
	}

}
