package br.com.hbsis.pedido;

import br.com.hbsis.periodo.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface IRepositoryPedido extends JpaRepository<Pedido, Long> {

    List<Pedido> findByPeriodo(Periodo periodo);
}
