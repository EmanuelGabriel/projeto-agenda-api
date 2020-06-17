package br.com.srsolution.agenda.domain.service.contato;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.srsolution.agenda.domain.exception.EntidadeNaoEncontradaException;
import br.com.srsolution.agenda.domain.exception.RegraNegocioException;
import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.domain.repository.ContatoRepository;
import br.com.srsolution.agenda.dtos.ContatoDTO;

@Service
public class ContatoServiceImpl implements ContatoService {

	private static final String CONTATO_COD_NAO_ENCONTRADO = "Contato de código não encontrado";
	private static final String EMAIL_JA_EXISTENTE = "Já existe um contato registrado com este e-mail";

	@Autowired
	private ContatoRepository contatoRepository;

	@Override
	public Contato salvar(Contato contato) {

		Contato contatoExistente = this.contatoRepository.findByEmail(contato.getEmail());

		if (contatoExistente != null && !contatoExistente.equals(contato)) {
			throw new RegraNegocioException(EMAIL_JA_EXISTENTE);
		}

		contato.setFavorito(contato.isFavorito());

		return this.contatoRepository.save(contato);
	}

	@Override
	public Page<Contato> lista(Pageable pageable) {
		return this.contatoRepository.findAll(pageable);
	}

	@Override
	public ContatoDTO buscarPorCodigo(Long codigo) {

		Optional<Contato> contato = this.contatoRepository.findById(codigo);
		if (!contato.isPresent()) {
			throw new EntidadeNaoEncontradaException(CONTATO_COD_NAO_ENCONTRADO);
		}

		ContatoDTO contatoDto = ContatoDTO.mapToDto(contato.get());

		return contatoDto;
	}

	@Override
	public List<ContatoDTO> buscarPorNome(String nome) {
		List<ContatoDTO> contatosPorNome = ContatoDTO
				.mapToCollectionEntidade(this.contatoRepository.findByNomeContaining(nome));
		if (contatosPorNome.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Não foi encontrado um contato com este nome");
		}

		return contatosPorNome;
	}

	@Override
	public List<ContatoDTO> buscarPorTelefone(String telefone) {
		List<ContatoDTO> contatoPorNome = ContatoDTO
				.mapToCollectionEntidade(this.contatoRepository.findByTelefone(telefone));
		if (contatoPorNome.isEmpty()) {
			throw new EntidadeNaoEncontradaException("Não foi encontrado um contato com este telefone");
		}
		return contatoPorNome;
	}

	@Override
	public void favoritar(Long codigo) {
		Optional<Contato> contato = this.contatoRepository.findById(codigo);
		contato.ifPresent(c -> {
			boolean favorito = c.isFavorito() == Boolean.TRUE;
			c.setFavorito(!favorito);
			this.contatoRepository.save(contato.get());
		});

		contato.orElseThrow(() -> new EntidadeNaoEncontradaException(CONTATO_COD_NAO_ENCONTRADO));

	}

	@Override
	public Contato atualizar(Long codigo, Contato contato) {

		Optional<Contato> updateContato = this.contatoRepository.findById(codigo);
		if (!updateContato.isPresent()) {
			throw new EntidadeNaoEncontradaException(CONTATO_COD_NAO_ENCONTRADO);
		}

		contato.setCodigo(codigo);
		// contato.setFavorito(!contato.isFavorito());
		contato = this.contatoRepository.save(contato);

		return contato;
	}

	@Override
	public Contato update(Long codigo, ContatoDTO contatoDTO) {

		buscarPorCodigo(codigo);
		Contato contato = ContatoDTO.mapToModel(contatoDTO);
		contato.setCodigo(codigo);
		this.contatoRepository.save(contato);
		return contato;

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
}
