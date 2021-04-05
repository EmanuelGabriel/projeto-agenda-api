package br.com.srsolution.agenda.domain.service.cliente;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.srsolution.agenda.api.dtos.response.ClienteModelResponse;
import br.com.srsolution.agenda.domain.model.Cliente;

public interface ClienteService {

	Cliente salvar(Cliente cliente);

	Page<Cliente> listarTodos(Pageable pageable);

	Cliente buscarPorCodigo(Long codigo);

	Cliente buscarPorCpf(String cpf);

	List<ClienteModelResponse> findByAtivo();

	Cliente atualizar(Long codigo, Cliente cliente);

	byte[] relatorioPorCPFCliente(String cpf) throws Exception;

	void atualizarPropriedadeAtivo(Long codigo, Boolean ativo);

	void ativarStatus(Long codigo);

	void excluir(Long codigo);
	
	long quantidade();

}
