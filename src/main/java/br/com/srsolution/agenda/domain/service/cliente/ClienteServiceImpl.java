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

		var clienteExistente = this.clienteRepository.findByCpf(cliente.getCpf());
		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new RegraNegocioException(CLIENTE_CPF_EXISTENTE);
		}

		cliente.setAtivo(Boolean.FALSE);
		cliente.setDataCadastro(LocalDateTime.now());
		return this.clienteRepository.save(cliente);
	}

	@Override
	public void excluir(Long codigo) {
		this.clienteRepository.findById(codigo).map(cliente -> {
			this.clienteRepository.delete(cliente);
			return Void.TYPE;
		}).orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));

	}

	@Override
	public Cliente buscarPorCodigo(Long codigo) {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(CLIENTE_COD_NAO_ENCONTRADO));
	}
}
