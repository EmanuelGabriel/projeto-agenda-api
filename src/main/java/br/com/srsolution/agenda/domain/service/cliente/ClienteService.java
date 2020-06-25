package br.com.srsolution.agenda.domain.service.cliente;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.srsolution.agenda.api.dtos.ClienteDTO;
import br.com.srsolution.agenda.domain.model.Cliente;

public interface ClienteService {

	Cliente salvar(Cliente cliente);

	Page<Cliente> listarTodos(Pageable pageable);

	Cliente buscarPorCodigo(Long codigo);

	ClienteDTO buscarPorCpf(String cpf);

	List<ClienteDTO> findByAtivo();

	Cliente atualizar(Long codigo, Cliente cliente);

	void atualizarPropriedadeAtivo(Long codigo, Boolean ativo);

	void ativarStatus(Long codigo);

	void excluir(Long codigo);

}
