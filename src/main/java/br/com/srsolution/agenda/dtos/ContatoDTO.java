package br.com.srsolution.agenda.dtos;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import br.com.srsolution.agenda.domain.model.Contato;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContatoDTO {

	@NotNull
	private String nome;

	@Email
	@NotNull
	private String email;

	@NotBlank
	private Boolean favorito;

	public static ContatoDTO mapToDto(Contato contato) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(contato, ContatoDTO.class);
	}

	public static Contato mapToEntidade(ContatoDTO contatoDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(contatoDTO, Contato.class);
	}

	public static List<ContatoDTO> mapToCollectionEntidade(List<Contato> contatos) {
		return contatos.stream().map(cont -> mapToDto(cont)).collect(Collectors.toList());
	}

}
