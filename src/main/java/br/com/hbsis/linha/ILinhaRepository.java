package br.com.hbsis.linha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILinhaRepository extends JpaRepository<Linha, Long> {

}
