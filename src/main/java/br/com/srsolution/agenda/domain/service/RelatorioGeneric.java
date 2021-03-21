package br.com.srsolution.agenda.domain.service;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;

public interface RelatorioGeneric {

	byte[] gerarRelatorio(String nomeRelatorio, ServletContext servletContext) throws JRException, SQLException;

}
