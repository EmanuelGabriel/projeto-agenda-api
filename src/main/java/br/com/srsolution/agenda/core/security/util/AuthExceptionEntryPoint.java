package br.com.srsolution.agenda.core.security.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

	private static final String MENSAGEM_RECURSO_DE_AUTENTICACAO = "É necessária autenticação completa para acessar este recurso";
	private static final String NÃO_AUTORIZADO = "Não autorizado!";
	private static final String APPLICATION_JSON = "application/json";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		final Map<String, Object> mapBodyException = new HashMap<>();
		mapBodyException.put("erro", NÃO_AUTORIZADO);
		mapBodyException.put("mensagem", MENSAGEM_RECURSO_DE_AUTENTICACAO);
		mapBodyException.put("path", request.getServletPath());

		response.setContentType(APPLICATION_JSON);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), mapBodyException);

	}

}
