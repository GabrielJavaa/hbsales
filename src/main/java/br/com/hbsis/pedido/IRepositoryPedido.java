package br.com.hbsis.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

interface IRepositoryPedido extends JpaRepository<Pedido, Long> {
}
