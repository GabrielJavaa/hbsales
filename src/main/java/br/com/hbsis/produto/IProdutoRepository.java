package br.com.hbsis.produto;

import br.com.hbsis.linha.Linha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsBylinhaCategoria(Linha linha);
}
