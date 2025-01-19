package com.online.PedidosKlock.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.PedidosKlock.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByClienteId(Long clienteId); // Verifica se existem pedidos para o cliente

}

