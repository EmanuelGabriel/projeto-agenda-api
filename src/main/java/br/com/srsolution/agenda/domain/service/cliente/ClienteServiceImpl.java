package br.com.srsolution.agenda.domain.service.cliente;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.srsolution.agenda.api.dtos.ClienteDTO;
import br.com.srsolution.agenda.api.modelmapper.ClienteModelMapper;
import br.com.srsolution.agenda.domain.exception.EntidadeNaoEncontradaException;
import br.com.srsolution.agenda.domain.exception.RegraNegocioException;
import br.com.srsolution.agenda.domain.model.Cliente;
import br.com.srsolution.agenda.domain.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

	private static final String CLIENTE_COD_NAO_ENCONTRADO = "Cliente de código não encontrado";
	private static final String CLIENTE_CPF_EXISTENTE = "Já existe um cliente registrado com este CPF";
	private static final String CLIENTE_CPF_NAO_ENCONTRADO = "Não foi encontrado cliente registrado com este CPF";

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteModelMapper modelMapper;

	@Override
	public Page<Cliente> listarTodos(Pageable pageable) {
		return this.clienteRepository.findAll(pageable);
	}

	public List<ClienteDTO> listarTodoss() {
		return this.modelMapper.toCollectionModel(this.clienteRepository.findAll());
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
	public List<ClienteDTO> findByAtivo() {
		return this.modelMapper.toCollectionModel(this.clienteRepository.findByAtivoTrue());
	}

	@Override
	public ClienteDTO buscarPorCpf(String cpf) {
		var cliente = this.clienteRepository.findByCpf(cpf);
		if (cliente == null) {
			throw new EntidadeNaoEncontradaException(CLIENTE_CPF_NAO_ENCONTRADO);
		}
		var clienteDto = this.modelMapper.toModel(cliente);
		return clienteDto;
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
	public void excluir(Long codigo) {
		this.clienteRepository.findById(codigo).map(cliente -> {
			this.clienteRepository.delete(cliente);
			return Void.TYPE;
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

}
