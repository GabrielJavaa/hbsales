package br.com.hbsis.periodo;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPeriodoRepository extends JpaRepository<Periodo, Long> {
    List<Periodo> findByFornecedor(Fornecedor fornecedor);
}
