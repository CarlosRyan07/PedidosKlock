package com.online.PedidosOn.service.util;

import com.online.PedidosOn.model.Pedido;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PedidoProcessor {

    public void processarEValidar(Pedido pedido) {
        calcularTotais(pedido);
        verificarEstoque(pedido);
        definirDataEntrega(pedido);
    }

    public void atualizarDadosPedido(Pedido pedidoExistente, Pedido pedidoAtualizado) {
        pedidoExistente.setCliente(pedidoAtualizado.getCliente());
        pedidoExistente.setItens(pedidoAtualizado.getItens());
    }

    private void calcularTotais(Pedido pedido) {
        double total = pedido.getItens().stream()
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();

        if (pedido.getCliente().isVip()) {
            total *= 0.9; // Aplica desconto de 10% para clientes VIP
        }

        pedido.setTotal(total);
        pedido.setTotalComDesconto(total);
    }

    private void verificarEstoque(Pedido pedido) {
        boolean emEstoque = pedido.getItens().stream()
                .allMatch(item -> item.getQuantidade() <= item.getEstoque());
        pedido.setEmEstoque(emEstoque);
    }

    private void definirDataEntrega(Pedido pedido) {
        if (pedido.isEmEstoque()) {
            pedido.setDataEntrega(LocalDate.now().plusDays(3));
        } else {
            pedido.setDataEntrega(null);
        }
    }
}

