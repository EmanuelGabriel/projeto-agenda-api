package br.com.srsolution.agenda.domain.service;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;

public interface RelatorioGeneric {

	byte[] gerarRelatorio(String nomeRelatorio, Map<String, Object> parametros, ServletContext servletContext)
			throws JRException, SQLException;

	byte[] gerarRelatorio(String nomeRelatorio, ServletContext servletContext) throws JRException, SQLException;

}
