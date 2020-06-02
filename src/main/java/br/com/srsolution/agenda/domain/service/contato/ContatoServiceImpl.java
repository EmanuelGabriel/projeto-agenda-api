package br.com.srsolution.agenda.domain.service.contato;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.srsolution.agenda.domain.exception.EntidadeNaoEncontradaException;
import br.com.srsolution.agenda.domain.exception.RegraNegocioException;
import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.domain.repository.ContatoRepository;
import br.com.srsolution.agenda.dtos.ContatoDTO;

@Service
public class ContatoServiceImpl implements ContatoService {

	@Autowired
	private ContatoRepository contatoRepository;

	@Override
	public Contato salvar(Contato contato) {

		Contato contatoExistente = this.contatoRepository.findByEmail(contato.getEmail());

		if (contatoExistente != null && !contatoExistente.equals(contato)) {
			throw new RegraNegocioException("Já existe um contato registrado com este e-mail");
		}

		contatoExistente = this.contatoRepository.save(contato);

		return contatoExistente;
	}

	@Override
	public List<ContatoDTO> lista(Pageable pageable) {
		return this.contatoRepository.findAll(pageable).stream().map(ContatoDTO::mapToDto).collect(Collectors.toList());
	}

	@Override
	public ContatoDTO buscarPorCodigo(Long codigo) {

		Optional<Contato> contato = this.contatoRepository.findById(codigo);
		if (!contato.isPresent()) {
			throw new EntidadeNaoEncontradaException("Contato de código não encontrado");
		}

		ContatoDTO contatoDto = ContatoDTO.mapToDto(contato.get());

		return contatoDto;
	}

	@Override
	public void excluir(Long codigo) {

		try {

			buscarPorCodigo(codigo);
			this.contatoRepository.deleteById(codigo);

		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Contato não pode ser removido, pois está em uso");
		}

	}

	@Override
	public List<ContatoDTO> buscarPorNome(String nome) {
		List<ContatoDTO> contatosPorNome = ContatoDTO
				.mapToCollectionEntidade(this.contatoRepository.findByNomeContaining(nome));
		if (contatosPorNome.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Contato de código não encontrado");
		}

		return contatosPorNome;
	}

	@Override
	public void favoritarContato(Long codigo, Boolean favorito) {
		Optional<Contato> contato = this.contatoRepository.findById(codigo);
		contato.ifPresent(c -> {
			boolean favoritar = c.getFavorito() == Boolean.TRUE;
			c.setFavorito(!favoritar);
			this.contatoRepository.save(contato.get());
		});

		contato.orElseThrow(() -> new EntidadeNaoEncontradaException("Contato de código não encontrado"));
	}

	@Override
	public void favoritar(Long codigo) {
		Optional<Contato> contato = this.contatoRepository.findById(codigo);
		contato.ifPresent(c -> {
			boolean favorito = c.getFavorito() == Boolean.TRUE;
			c.setFavorito(!favorito);
			this.contatoRepository.save(contato.get());
		});

		contato.orElseThrow(() -> new EntidadeNaoEncontradaException("Contato de código não encontrado"));

	}

}
