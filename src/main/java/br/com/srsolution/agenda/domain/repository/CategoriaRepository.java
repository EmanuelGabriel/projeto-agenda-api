package br.com.srsolution.agenda.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.srsolution.agenda.domain.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
