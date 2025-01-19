package com.online.PedidosOn.repository;


import com.online.PedidosOn.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByClienteId(Long clienteId); // Verifica se existem pedidos para o cliente

}

