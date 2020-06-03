package br.com.srsolution.agenda.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.srsolution.agenda.domain.model.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

	Contato findByEmail(String email);

	List<Contato> findByNomeContaining(String nome);

	List<Contato> findByTelefone(String telefone);

}
