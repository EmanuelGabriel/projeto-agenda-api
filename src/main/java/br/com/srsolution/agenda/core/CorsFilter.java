package br.com.srsolution.agenda.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	// TODO: Configurar para diferentes Ambientes/Origins
	// private static final String ORIGIN_PERMITIDA = "http://localhost:8000";
	private static final String ORIGIN_PERMITIDA = "http://localhost:4200";
	private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String CONTROL_ACCESS_ORIGIN = "Access-Control-Allow-Origin";
	private static final String CONTROL_ACCESS_CREDENTIALS = "Access-Control-Allow-Credentials";

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		response.setHeader(CONTROL_ACCESS_ORIGIN, ORIGIN_PERMITIDA);
		response.setHeader(CONTROL_ACCESS_CREDENTIALS, "true");

		if ("OPTIONS".equals(request.getMethod()) && ORIGIN_PERMITIDA.equals(request.getHeader("Origin"))) {
			response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, DELETE, PUT, PATCH, OPTIONS");
			response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type, Accept");
			response.setHeader(ACCESS_CONTROL_MAX_AGE, "3600"); // Cache de 1h

			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
