package br.com.srsolution.agenda.domain.service.cliente;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.srsolution.agenda.domain.service.RelatorioGeneric;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioClienteService implements RelatorioGeneric {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public byte[] gerarRelatorio(String nomeRelatorio, ServletContext servletContext) throws JRException, SQLException {

		// obter conexão com o banco de dados
		Connection connection = jdbcTemplate.getDataSource().getConnection();

		// carregar o caminho do arquivo Jasper
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";

		// gerar o relatório com os dados e conexão
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(), connection);

		// Exporta para byte o PDF para fazer o download
		return JasperExportManager.exportReportToPdf(print);
	}

}
