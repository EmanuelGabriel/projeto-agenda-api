package br.com.srsolution.agenda.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.modelmapper.ModelMapper;

import br.com.srsolution.agenda.domain.model.Contato;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContatoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 5, max = 90)
	private String nome;

	@Email
	@NotBlank
	private String email;

	@NotNull
	private Boolean favorito;

	@NotBlank
	private String telefone;

	public static ContatoDTO mapToDto(Contato contato) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(contato, ContatoDTO.class);
	}

	public static Contato mapToModel(ContatoDTO contatoDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(contatoDTO, Contato.class);
	}

	public static List<ContatoDTO> mapToCollectionEntidade(List<Contato> contatos) {
		return contatos.stream().map(contato -> mapToDto(contato)).collect(Collectors.toList());
	}

}
