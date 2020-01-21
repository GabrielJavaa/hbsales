package br.com.hbsis.item;

import br.com.hbsis.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface IItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByPedido(Pedido pedido);
}
