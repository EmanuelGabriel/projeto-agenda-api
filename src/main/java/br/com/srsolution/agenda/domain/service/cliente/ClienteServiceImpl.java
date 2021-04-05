package br.com.srsolution.agenda.domain.service.cliente;

import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.srsolution.agenda.api.dtos.response.ClienteModelResponse;
import br.com.srsolution.agenda.api.modelmapper.ClienteModelMapper;
import br.com.srsolution.agenda.domain.exception.EntidadeNaoEncontradaException;
import br.com.srsolution.agenda.domain.exception.RegraNegocioException;
import br.com.srsolution.agenda.domain.model.Cliente;
import br.com.srsolution.agenda.domain.repository.ClienteRepository;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ClienteServiceImpl implements ClienteService {

	private static final String CLIENTE_COD_NAO_ENCONTRADO = "Cliente de código não encontrado";
	private static final String CLIENTE_CPF_EXISTENTE = "Já existe um cliente registrado com este CPF";
	private static final String CLIENTE_CPF_NAO_ENCONTRADO = "Não foi encontrado cliente registrado com este CPF";
	private static final String PATH_RELATORIO_CPF_CLIENTE = "/relatorios/relatorio-por-cpf-cliente.jasper";

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteModelMapper modelMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public byte[] relatorioPorCPFCliente(String cpf) throws Exception {

		// obter conexão com o banco de dados
		Connection connection = jdbcTemplate.getDataSource().getConnection();

		Cliente dadoCPFCliente = this.clienteRepository.findByCpf(cpf);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("CPF", dadoCPFCliente.getCpf());

		InputStream inputStreamRelatorio = this.getClass().getResourceAsStream(PATH_RELATORIO_CPF_CLIENTE);

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStreamRelatorio, parametros, connection);

		return JasperExportManager.exportReportToPdf(jasperPrint);

	}

	@Override
	public Page<Cliente> listarTodos(Pageable pageable) {
		return this.clienteRepository.findAll(pageable);
	}

	@Override
	public Cliente salvar(Cliente cliente) {

		var cpfClienteExistente = this.clienteRepository.findByCpf(cliente.getCpf());

		if (cpfClienteExistente != null && !cpfClienteExistente.equals(cliente)) {
			throw new RegraNegocioException(CLIENTE_CPF_EXISTENTE);
		}

		cliente.setAtivo(Boolean.FALSE);
		cliente.setDataCadastro(LocalDateTime.now());

		return this.clienteRepository.save(cliente);

	}

	@Override
	public Cliente buscarPorCodigo(Long codigo) {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));
	}

	@Override
	public List<ClienteModelResponse> findByAtivo() {
		return this.modelMapper.toCollectionModelResponse(this.clienteRepository.findByAtivoTrue());
	}

	@Override
	public Cliente buscarPorCpf(String cpf) {
		var cliente = this.clienteRepository.findByCpf(cpf);
		if (cliente == null) {
			throw new EntidadeNaoEncontradaException(CLIENTE_CPF_NAO_ENCONTRADO);
		}
		return cliente;
	}

	@Override
	public Cliente atualizar(Long codigo, Cliente cliente) {
		return this.clienteRepository.findById(codigo).map(cli -> {
			cli.setNome(cliente.getNome());
			cli.setEmail(cliente.getEmail());
			return this.clienteRepository.save(cli);
		}).orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));
	}

	@Override
	public void ativarStatus(Long codigo) {
		var cliente = this.clienteRepository.findById(codigo);
		cliente.ifPresent(cli -> {
			boolean ativo = cli.getAtivo() == Boolean.TRUE;
			cli.setAtivo(!ativo);
			this.clienteRepository.save(cliente.get());
		});

		cliente.orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));
	}

	@Override
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		var cliente = buscarPorCodigo(codigo);
		cliente.setAtivo(ativo);
		this.clienteRepository.save(cliente);
	}

	@Override
	public void excluir(Long codigo) {
		this.clienteRepository.findById(codigo).map(cliente -> {
			this.clienteRepository.delete(cliente);
			return Void.TYPE;
		}).orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));

	}

	@Override
	public long quantidade() {
		var qtd = this.clienteRepository.count();
		return qtd;
	}

}
