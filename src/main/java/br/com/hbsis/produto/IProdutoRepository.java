package br.com.hbsis.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
 interface IProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigoProduto(String codigoProduto);


    Produto findByCodigoProduto(String codigoProduto);
}
