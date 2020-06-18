package br.com.srsolution.agenda.api.modelmapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.srsolution.agenda.api.dtos.ContatoDTO;
import br.com.srsolution.agenda.api.dtos.ContatoInputDTO;
import br.com.srsolution.agenda.domain.model.Contato;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContatoModelMapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ModelMapper modelMapper;

	public ContatoDTO toModel(Contato contato) {
		return this.modelMapper.map(contato, ContatoDTO.class);
	}

	public Contato toDto(ContatoDTO contatoDTO) {
		return this.modelMapper.map(contatoDTO, Contato.class);
	}

	public Contato toDto(ContatoInputDTO contatoInputDTO) {
		return this.modelMapper.map(contatoInputDTO, Contato.class);
	}

	public List<ContatoDTO> toCollectionModel(List<Contato> contatos) {
		return contatos.stream().map(this::toModel).collect(Collectors.toList());

	}
}
