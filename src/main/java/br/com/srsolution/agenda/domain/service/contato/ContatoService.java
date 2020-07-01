package br.com.srsolution.agenda.domain.service.contato;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.srsolution.agenda.api.dtos.ContatoDTO;
import br.com.srsolution.agenda.domain.model.Contato;

public interface ContatoService {

	Contato salvar(Contato contato);

	Page<Contato> lista(Pageable pageable);

	ContatoDTO buscarPorCodigo(Long codigo);

	Contato findByCodigo(Long codigo);

	List<ContatoDTO> buscarPorNome(String nome);

	List<ContatoDTO> buscarPorTelefone(String telefone);

	Contato atualizar(Long codigo, Contato contato);

	void excluir(Long codigo);

	void favoritar(Long codigo);

}
