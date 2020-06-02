package br.com.srsolution.agenda.domain.service.contato;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.dtos.ContatoDTO;

public interface ContatoService {

	Contato salvar(Contato contato);

	List<ContatoDTO> lista(Pageable pageable);

	ContatoDTO buscarPorCodigo(Long codigo);

	List<ContatoDTO> buscarPorNome(String nome);

	void excluir(Long codigo);

	void favoritarContato(Long codigo, Boolean favorito);

	void favoritar(Long codigo);

}
