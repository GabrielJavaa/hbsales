package br.com.hbsis.linha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILinhaRepository extends JpaRepository<Linha, Long> {


    Optional<Linha>findBycodigolinha(String codigolinha);
}
