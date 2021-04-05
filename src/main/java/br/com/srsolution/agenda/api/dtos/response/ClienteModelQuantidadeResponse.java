package br.com.srsolution.agenda.api.dtos.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModelQuantidadeResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long quantidade;

}
